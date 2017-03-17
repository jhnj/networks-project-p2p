# Status report 17.3

Walter Berggren 538637  
Johan Jern 479262

---

We are creating a basic p2p file-sharing system with Java. We have currently only
thought through the design of the system and decided on the protocol we will be using.

We have a pretty clear idea of how our system should be implemented and coding it in
Java should not be a problem. Something that can be problematic with a p2p program is
opening tcp-connections to peers behind NAT:s. This is something we will not initially
focus on and only when we have a working implementation we will try using NAT traversal
or something similar to circumvent this issue.

More problems will probably arise during the development process but we are confident
that they will be solvable.


