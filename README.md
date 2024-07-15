# uMessage Chat Application

## Overview

The uMessage Chat Application is designed to implement various data structures and algorithms, which are used to drive word suggestion, spelling correction, and autocompletion within the app. This was built as a major project in CSE 332, Data Structures and Parallelism at the University of Washington Seattle. The project covers the following:

- **Data Structures:** AVLTree, HashTrieMap, ChainingHashTable, MinFourHeap, CircularArrayFIFOQueue, and MoveToFrontList.
- **Sorting Algorithms:** HeapSort, QuickSort, and TopKSort.
- **Backend for Chat Application:** These implementations power the backend of uMessage, enhancing word suggestion, spelling correction, and autocompletion features.

## Key Features

- **Auto-Suggestion and Autocompletion:** Enhanced user experience through intelligent word suggestions.
- **Custom Data Structures:** Built from scratch to optimize performance and functionality.
- **Algorithm Implementation:** Efficient sorting and searching to support real-time chat application features.

## Custom Data Structures

The custom data structures implemented for this project can be found in the following directories:

### Dictionaries:
- `src/main/java/datastructures/dictionaries/AVLTree.java`
- `src/main/java/datastructures/dictionaries/ChainingHashTable.java`
- `src/main/java/datastructures/dictionaries/HashTrieMap.java`
- `src/main/java/datastructures/dictionaries/HashTrieSet.java`
- `src/main/java/datastructures/dictionaries/MoveToFrontList.java`

### Worklists:
- `src/main/java/datastructures/worklists/ArrayStack.java`
- `src/main/java/datastructures/worklists/CircularArrayFIFOQueue.java`
- `src/main/java/datastructures/worklists/ListFIFOQueue.java`
- `src/main/java/datastructures/worklists/MinFourHeap.java`
- `src/main/java/datastructures/worklists/MinFourHeapComparable.java`

### Sorts:
- `src/main/java/p2/sorts/HeapSort.java`
- `src/main/java/p2/sorts/QuickSort.java`
- `src/main/java/p2/sorts/TopKSort.java`

### Word Suggestor:
- `src/main/java/p2/wordsuggestor/NGramToNextChoicesMap.java`
