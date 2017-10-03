A p2p client + tracker made for the course `ELEC-C7241` **Computer Networks** at Aalto University.

# Tietoliikenneprojekti 2017

Johan Jern 
Walter Berggren

## Introduction
WJ-p2p is a P2P file sharing solution implemented in Java.

The system consists of two parts: a tracker and a client program. In short, the tracker keeps track of a set of files and users. When the client connects to the server, it retrieves the list of files from the server. Once the user decides to download a file, it asks for the list of users that have either the whole file or parts of it. Then, the client connects to the other clients and asks what parts of the file it has. These parts are called *blocks*. After this, the client requests and downloads blocks from its peers until the whole file has been downloaded.

## Compiling and Running

To run the software JDK8 is required, the software is tested on kosh.aalto.fi and works there
out of the box. Start by unzipping the file and entering the `tietoliikenneprojekti` directory.
Run `./gradlew installDist` to compile
the project and create executable files for the client software as well as the tracker software. The executables can be found at:

Tracker: `tietoliikenneprojekti/tracker/build/install/tracker/bin/tracker`  
Client: `tietoliikenneprojekti/client/build/install/client/bin/client`

First start the tracker then run clients as needed.

## Usage

### Tracker
No input from the user is needed.

The tracker listens to the port `3004`. If behind a NAT, this port has to be port forwarded.

### Client
```
Choose port: 1888
Enter tracker ip: localhost
Press A to add Files and D to download files
```

First, the user is asked to choose a port. This port is used to accept requests from other clients. Make sure to forward this port if behind a NAT.

Secondly, specify the address of the tracker. This can either be an IP address or a hostname.

Thirdly, the client asks whether you want to add or download a file. Type `A` to add a file and `D` to download a file.

## Structure and implementation
The project is separated into three parts: the tracker, the client, and *common*. Common includes all classes shared between the tracker and the client. The multi-build system and dependencies are managed using Gradle. The third-party dependency is used [Jackson](https://github.com/FasterXML/jackson) for JSON parsing.

### Tracker

#### Class Overview
* Tracker (main)
* TrackerServer
* ClientThread

#### Tracker.java
The tracker class contains the `main(String[] args)` function of the tracker. When run, the function instantiates TrackerServer and starts listening to the specified port.

At the moment, the Tracker class does not contain any other logic than this. If any configuration options (such as specifying the port) are added later, all logic for handling that would be added here. This way, this logic is separated from the part that serves the clients.

#### TrackerServer.java
When instantiated, TrackerServer starts listening TCP connections on the specified port. New threads (`ClientThread`) are spawned to serve connecting clients, passing the instance of `TrackerServer` to `ClientThread`.

TrackerServer also manages the state of the tracker. All files added to the server are stored in a map pointing from files to the set of clients that have the file. This map is accessed and manipulated through the following methods that are exposed to `ClientThread` instances:

* `addFile` - Adds a file to the map
* `addClientToFile` - Associates a client with a file (called when a client indicates having a certain file)
* `getClientsWithFile` - Returns the clients associated with a file
* `removeClientFromFile` - Removes a client associated with a file (called when the client disconnects)

The map is synchronized (instantiated using `Collections.synchronizedMap`) for safely manipulating the map from the `ClientThreads`.

Currently, the information is not persisted in any way. All data such as added files and users is thus lost if the tracker is restarted. If persisting to a file or database is added in the future, the operations for persisting the data would be added to the four methods mentioned above. Loading of persisted data would be added to the class constructor.

#### ClientThread.java
`ClientThread` takes care of all incoming requests from the client. The possible requests and their responses are specified in the protocol section. The instance calls the public methods of `ClientServer` for retrieving and updating file and client information in the tracker.

Incoming requests are repeatedly read in a `while (true)` loop. The socket's `InputStream` is passed to an instance of `WJReader` and the socket's `OutputStream` to `WJWriter`. With `WJReader` and `WJWriter`, JSON and binary data can be read and written by calling a single function (see details under common). `ClientThread` checks that the type of the incoming request is JSON (all requests are in JSON), then retrieves the JSON string. The type of request is always specified in the `action` field of the JSON. The JSON string is then parsed using the `WJMessage` class to an instance of a Java class corresponding to the request (all requests and responses are defined in `common/json`).

`ClientThread` has private methods for handling all the possible incoming requests. These are called once the request has been instantiated. They perform any needed action (such as trying to add a file), calling methods from `ClientServer` as needed. The appropriate response class is then instantiated, stringified using `WJMessage`, and sent back to the client.

### Client

#### Class Overview
* Client (main)
* ClientSession
* FileDownloader
* FileHandler
* FileProvider
* FileProviderThread

#### Client.java
The client class contains the `main(String[] args)` function of the client. This class handles all input and output between the client and the user. When run, it asks the client to specify the IP of the tracker to use. The user also specifies what port it wants to listen to. This is needed because the users currently have to setup port forwarding if they are behind a NAT.

After setup, `Client` attempts to open a socket connection to the tracker. This socket is passed to an instance of `ClientSession` which can be called for communicating with the server. `Client` also spawns a new thread running `FileProvider` which will take care of responding to any incoming requests from other clients. The `FileProvider` is passed an instance of `FileHandler` which contains methods for local file manipulation (adding files, storing and retrieving blocks).

In a `while (true)` loop, the client then repeatedly asks the user whether to download a file or add a new one to the tracker.

Upon adding a new file, `Client` asks the user to specify what file to add. Then, the `addLocalFile` method of `FileHandler` is called to perform the add action.

In the case of downloading a file, `ClientSession` is used to retrieve a file list from the tracker. The file list is presented to the user. Once the user has picked a file, an instance of `FileDownloader` is created. The `downloadFile` method of `FileDownloader` is called to perform the download. This method is run in the main thread blocking the rest of the application. Consequently, other files cannot be downloaded simultaneously. To support simultaneous downloads `FileDownloader` could be extended to implement `Runnable` and thus be run in a separate thread.

The `Client` class also catches all `IOExceptions` thrown by the socket or when calling methods from `ClientSession`. This way, we know if the connection to the tracker is broken. At the moment, the client does not attempt to recover from this situation. It simply quits.

#### ClientSession.java
`ClientSession` exposes methods for performing all the requests that the client can make to the tracker. What these requests are can be found in the protocol section.

The methods all work in a similar manner. They instantiate the relevant request class from `common/json` and stringify it using methods in `WJMessage`. `WJWriter` is then used to write the stringified JSON to the server. Responses are read using `WJReader` and then parsed using `WJMessage` to the appropriate response class from `common/json`. Relevant data is then returned from the response. For instance, `requestFileList` returns an array of `WJFiles` (a class representing individual files, see the protocol section for details).

#### FileDownloader.java
`FileDownloader` is used for attempting to download a file from the network.

First, `FileDownloader` attempts to create a file locally. This is done by calling the passed `FileHandler's` add remote file method (see `FileHandler` for details). Secondly, a set is created containing all the indices of the block that are still to be downloaded. Thirdly, the program is run through a loop that continues until the set containing all the block indices is empty.

For every iteration, the `attemptToDownloadBlocks` method is called. As the name suggests, it attempts to download all the remaining blocks. This is done by first querying the tracker for a list of all clients that have the file. Each client is then asked to provide a list of all blocks that they already have downloaded. This information is stored in a map pointing from a block index to an `ArrayList` of clients that have that particular block. The map is then filtered to remove all the blocks that already have been downloaded. After filtering, the entries are added to a [priority queue](https://docs.oracle.com/javase/7/docs/api/java/util/PriorityQueue.html) where blocks with few clients are prioritized.

When polling entries from the priority queue, the entry with the highest priority (fewest clients with that block) is returned and removed from the priority queue. By attempting to download the blocks that the fewest clients have the network attempts to continuously balance the block distribution in the network, helping to avoid a situation where there are some blocks that no clients have.

Block/client pairs are polled one by one from the priority queue. The program takes a client from the list of clients and attempts to download the block from the chosen client. If the download fails, it tries the next client until the download is successful. If no clients can provide the block, the block is skipped.

After an iteration of the block download phase has been completed, the downloaded blocks are removed from the set of blocks to be downloaded. The process is then repeated until all blocks have been downloaded.

#### FileHandler.java
`FileHandler` is responsible for providing methods for file manipulation for the client classes. It provides a safe interface for file system operations on `WJFiles` without having to know anything about the underlying file (represented internally by `WJFileOnDisk`).

`FileHandler` provides methods for adding both local and remote files as well as storing and retrieving individual blocks. However, the actual file system operations are implemented in `common`.

#### FileProvider.java
Responsible for accepting incoming connections from other clients. `FileProvider` has a `while (true)` loop that continuously accepts connections from clients, spawning new threads of `FileProviderThread` for every incoming connections.

#### FileProviderThread.java
Takes care of incoming requests from a single client (see protocol for a list of possible requests). This class is implemented in a similar manner as `ClientSession`.   

### Common
This module contains classes that are shared between both the tracker and the client.

#### Class Overview
* Exceptions
 * BlockException
 * FileNotInServerException
 * InvalidHashException
 * UserNotInFileException
 * WJException
* Files
 * WJFileOnDisk
 * WJFileOnDiskFactory
* JSON
 * AddFileRequest
 * BlockListRequest
 * BlockListResponse
 * BlockListResponse
 * BlockRequest
 * FileClientsRequest
 * FileClientsResponse
 * FileListRequest
 * FileListResponse
 * OKResponse
 * SetupPortRequest
 * WJClient
 * WJFile
 * WJMessage
* Reader
 * WJReader
 * WJType
* SHA1
 * SHA1
* Writer
 * WJWriter

#### Exceptions `PACKAGE`
The classes in this package specify the project-specific exceptions used in WJ-p2p.

#### Files `PACKAGE`

##### WJFileOnDisk.java
Represents a file on the disk. The class has the same properties as `WJFile`, but it also has a specified file path and provides methods for reading and writing blocks.

Reading and writing is done using the [RandomAccessFile](https://docs.oracle.com/javase/7/docs/api/java/io/RandomAccessFile.html) class. When called, the index of the block is specified (and the data when writing). The index is multiplied by the standard block size (2^16 bytes) to seek in the file before reading or writing.

##### WJFileOnDiskFactory.java
Provides the methods `initiateLocalFile` and `initiateRemoteFile`.

`initiateLocalFile` creates a `WJFileOnDisk` based on an existing file. The method calculates a SHA-1 hash for every 2^16 byte block of data in the file. The complete file hash is calculated of the block hashes joined together.

`initiateRemoteFile` creates an empty file locally based on a `WJFile` and returns a `WJFileOnDisk`.

#### JSON `PACKAGE`
Classes for representing all the possible JSON requests and responses (see protocol).

##### WJMessage.java
Contains methods for parsing and stringifying the JSON classes. The parsing and stringifying is done using the third-party Jackson library. All the methods in this class are static.

#### Reader `PACKAGE`

##### WJReader.java
Provides an interface for reading incoming messages from an `InputStream`. To retrieve a message, the `getType` method is called. This method attempts to read the protocol's header (three bytes) from the input stream, blocking until the data has been read. Then, the first byte is checked to determine whether the data is JSON or binary. Using the two last bytes of the header to determine the data length, the data is then read and stored in a private instance variable. The return type is then returned. The data can then be retrieved using the `getJsonString` and `getBinary` methods.

##### WJType.java
Enums for binary, JSON, and invalid data types.

#### SHA1 `PACKAGE`

##### Sha1.java
Provides static methods for calculating SHA-1 hashes of byte and string arrays.

#### Writer `PACKAGE`

##### WJWriter.java
Provides methods for writing binary data and JSON strings to an `OutputStream` according to the WJ-p2p protocol.

## Protocol
All communication is done over TCP connections in a request-response manner. The structure for the communication is the following:

| Type   | Length   | Data           |
|--------|----------|----------------|
| 8 bits | 16 bits  | Max 2^16 bytes |

The type field specifies the data type of the data field:

1. `0x00` - JSON encoded in UTF-8
2. `0x01` - Binary data

The length specifies the length of the data field in bytes.

The network byte order (big endian) is used.

### JSON Object Types
The protocol uses the following defined JSON object types:

#### File
| Parameters     | Value       | Type          | Required    | Description        |
|----------------|-------------|---------------|-------------|--------------------|
| name           | ...         | String        | Yes         |                    |
| size           | ...         | Int           | Yes         | File size in bytes |
| hash           | ...         | String        | Yes         | The SHA-1 hash of the file |
| blocks         | [...]       | Array[String] | Yes         | An array containing the block hashes of the file |

The block hashes are SHA-1 hashes of 2^16 byte blocks. The complete file hash is calculated by joining the block hashes together and hashing the joined string.

#### Client
A client currently seeding or leeching a file.

| Parameters     | Value       | Type          | Required    | Description        |
|----------------|-------------|---------------|-------------|--------------------|
| ip             | ...         | String        | Yes         | The IPv4/6 address in the format x.x.x.x or y:y:y:y:y:y:y:y
| port           | ...         | Int           | Yes         | The port that the client is listening to |

### Client to Tracker
The tracker listens to port `3004`.

#### Setup Port Request
The first message a client send should be which port-number it is listening to.

##### Request `JSON`
| Parameters     | Value       | Type          | Required    | Description |
|----------------|-------------|---------------|-------------|-------------|
| action         | port        | String        | Yes         ||
| port           | port-number | Int           | Yes         | The port that the client is listening to


#### File List Request
Requests the list of files from the tracker.

##### Request `JSON`
| Parameters     | Value       | Type          | Required    | Description |
|----------------|-------------|---------------|-------------|-------------|
| action         | file_list   | String        | Yes         ||
| existing_files | [...]       | Array[File]   | Optional    | Informs the server what files the user has

##### Response `JSON`
| Parameters     | Value       | Type        | Required    | Description |
|----------------|-------------|-------------|-------------|-------------|
| files          | [...]       | Array[File] | Yes         | An array containing the tracked files

#### File Clients Request
Requests the list of users currently seeding or leeching a file.

##### Request `JSON`
| Parameters     | Value        | Type        | Required    | Description |
|----------------|--------------|-------------|-------------|-------------|
| action         | file_clients | String      | Yes         ||
| file           | ...          | File        | Yes         |             |

##### Response `JSON`
| Parameters     | Value        | Type          | Required    | Description |
|----------------|--------------|---------------|-------------|-------------|
| clients        | [...]        | Array[Client] | Yes         | An array of the clients currently seeding or leeching the file |

#### Add File Request

Attempts to add a file to the server file list.

##### Request `JSON`
| Parameters     | Value       | Type          | Required    | Description |
|----------------|-------------|---------------|-------------|-------------|
| action         | add_file    | String        | Yes         ||
| file           | ...         | File          | Yes         | The file to add |

##### Response `JSON`
| Parameters     | Value       | Type        | Required    | Description |
|----------------|-------------|-------------|-------------|-------------|
| ok             | true/false  | Boolean     | Yes         |||


### Client to Client
Clients listen a randomly assigned port.

#### Block List Request
Requests a list of available blocks of the specified file.

##### Request `JSON`
| Parameters     | Value       | Type        | Required    | Description |
|----------------|-------------|-------------|-------------|-------------|
| action         | block_list  | String      | Yes         ||
| file_hash      | ...         | String      | Yes         | The SHA-1 hash identifier of the file |

##### Response `JSON`
| Parameters     | Value       | Type          | Required    | Description |
|----------------|-------------|---------------|-------------|-------------|
| blocks         | [...]       | Array[Int]    | Yes         | An array containing the indices of the available blocks

#### Block request
Requests a block of the specified file.

##### Request `JSON`
| Parameters     | Value       | Type          | Required    | Description |
|----------------|-------------|---------------|-------------|-------------|
| action         | block       | String        | Yes         ||
| file_hash      | ...         | String        | Yes         | The SHA-1 hash of the file |
| block_index     | ...         | String        | Yes         | The index of the block |

##### Response `BINARY`
The block in binary. If the request was invalid, the tracker does not respond.

## Testing

The software has between tested manually by running the tracker and multiple files over localhost.
The testing was done by going through multiple use case scenarios as well as some situations where stuff
 goes wrong, here is a short list of some test cases:

* Add a file to the tracker
* Download a file from the tracker
* Disconnect the original uploader
* Connect a new client and download the file
* Modify the file a client is providing, ensure that clients downloading it will only receive the original
version
* Delete the file a client is providing
* Disconnect a client while another client is downloading

The error handling in the software is rather simple, the user will be notified of the error and if 
possible the software will continue running but if it is a larger problem the software will simply stop.
An example of this is that the client will quit if the connection to the tracker is lost.

## Analysis

We chose to use tcp for the transport layer protocol as the delivery of all packets in the right order is important because we 
want to download exact copies of the uploaded files and latency is not a problem. As we are developing a p2p application
some clients will inevitably be connected through NAT:s which tcp works well with. There are really no downsides with using tcp
in this use case and the accurate delivery of tcp is vital for a file-sharing application.

The project uses java's `InetAddress` class when creating sockets which means that ipv6-addresses are handled exactly as
ipv4-addresses without any extra configuration. As NAT's can't be used with ipv6 the software is even easier to setup
with ipv6 as no port-forwarding need to be configured. The usage of ipv6 can for example be tested simply by running the tracker
locally and connecting to it with a client by setting the tracker ip as ipv6 localhost: `Enter tracker ip: ::1`.

If the client software disconnects from the tracker the tracker simply removes the client from its list of available clients.
If there is a disconnect when a client is downloading a file from another client the software simply discards the
block it tried to download and continues by trying to download from other available clients. This is ensured by rigorous exception
handling in the software and has been tested manually.

When a new client connects to the tracker or when a client requests a block from another client a new java
Thread is created handling that connection, this means that multiple connections is not a problem
as the software will simply individual threads for each connection.
