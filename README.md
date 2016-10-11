# ProblemAndAnswer
Selected Problems and My Answers

Q1 ***************************************

Description

Character recognition is the conversion of images into text. For now we consider each character in the picture is a N*M matrix with only zeros and ones, and we need to recognize K characters. You are to write a program to find minimal number of pixels so that we can recognize each character.

For example, we have only two characters 'T' and 'L', and the matrix size is 3*3, we can think 'T' and 'L' are

111    100
010    100
010    111
so we can recognize the character with only bottom-left pixel, the answer is 1.


Input

The first line of input is three integers N, M, K (1 <= N, M <= 10, 2 <= K <= 6). Which represents the size of matrix and number of characters. Then is following K blocks, which represents the matrix. Notice that each block starts with a blank line and we guarantee that characters are different.

Output

You should output the minimum number of pixels, which is the answer.


Q2 ***************************************

Description

We have N numbers as an array, you need to find a prefix array and a suffix array, which we can get the maximum xor value with all elements in them. Notice that for prefix[0, l] and suffix[r, n - 1], do not intersect (l < r), and they can be empty.

Input

The first line is one number N (1 <= N <= 100000) The second line contains N numbers ai(0 <= ai <= 1e12) separated by space, which represents the array.

Output

Just output the maximum xor result.


