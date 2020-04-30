Improved Seam carving

This project uses the algs4 library from Princeton University, which can be downloaded as a .jar here: http://algs4.cs.princeton.edu/code/
All other imported libraries are part of the Java Development Kit (JDK).

Run the program: Client.java [image filename] [num cols to remove] [num rows to remove] [parallel carving mode]

[image filename] - path to image
[num cols to remove] - count of columns that will be removed
[num rows to remove] - count of rows that will be removed
[parallel carving mode] - true/false values. If true - row and columns will be removed one by one, if false - all 
                          columns will be removed and after that will rows will be removed 

About algoritm

The central problem with the original concept of energy was it suggested what seam to remove, but it didn’t account for 
what happened after removing that seam. By removing a low-energy seam, pixels which were not adjacent are pushed together, 
and the total energy of the image may increase dramatically.

Instead, forward energy predicts what pixels will be adjacent after a seam removal, and uses that to suggest the best 
seam to remove. This is in contrast to the backward energy from before.