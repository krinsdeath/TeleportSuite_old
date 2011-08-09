Version 1.0.4
---
*   Made messages optional; setting any field to null, or deleting the key, will disable the message
*   Added a default value to toggle, at 'teleport.toggle.default'. Standard config sets it to false (requests allowed)
*   Each message is now multi-line capable by putting the value of the field into a list
*   Fixed /tpcancel not working properly

Version 1.0.3
---
*   Fixed /back not properly aligning pitch and yaw
*   Changed /cancel to /tpcancel, to fit the motif
*   Added 'world' option to /tploc
*   Added /loc (aliases /where and /whereami) to show the sender's current location
*   Fixed /tpo not correctly overriding the user's toggle switch in certain situations

Version 1.0.2
---
*   Added version tracking method (and update controller)
*   Increased cleaning when plugin is disabled
*   Added javadocs for possible API

Version 1.0.1
---
*   Fixed erroneous permission node for */tploc*
*   Added location tracking to */tploc* for /back
*   Added */cancel*
*   */tpaccept* and */tpreject* will now work without parameters by accepting or rejecting the first person in your request queue

Version 1.0.0
---
*   Initial release