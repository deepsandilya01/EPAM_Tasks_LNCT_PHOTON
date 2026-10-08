# Hardware Concurrency - Practical Validation

**Team LNCT Photon**

This repository contains the small Java programs that we used to
practically understand some of the concepts discussed in our research
on hardware concurrency, memory models and lock-free design.

## What We Tested

We worked on four small experiments:

1. Memory Visibility
2. CAS vs Lock
3. False Sharing
4. Ring Buffer

These experiments were kept small so that each concept could be
tested separately.

## 1. Memory Visibility

**File:** `MemoryVisibilityTest.java`

This experiment uses two threads.

The writer thread writes a value to shared data and then changes a
`ready` flag.

The reader waits for the flag and then reads the data.

We used `volatile` for the ready flag to understand how visibility
between threads works.

The main idea tested was:

```text
Writer
  |
  | data = 42
  | ready = true
  ↓
Reader
  |
  ↓
read data
```

**Result:**  
The reader was able to read the value written by the writer.

## 2. CAS vs Lock

**File:** `CasVsLockBenchmark.java`

In this test, multiple threads incremented the same counter.

We compared:

- `synchronized` counter
- CAS based counter using `AtomicInteger`

Both approaches were given the same number of increments.

We recorded the execution time and checked the final counter value.

The purpose was to understand how CAS can update a shared value
atomically without using a traditional synchronized section.

## 3. False Sharing

**File:** `FalseSharingTest.java`

Two threads update two different counters.

First, the counters are kept close together.

Then padding is added between them.

The execution time of both versions is compared.

The purpose was to understand how two logically separate variables
can still affect performance when they are located on the same
cache line.

This experiment is only a basic demonstration. The result can
depend on the JVM, processor and system.

## 4. Ring Buffer

**File:** `RingBufferDemo.java`

This experiment implements a small fixed-size ring buffer.

A producer puts values into the buffer and a consumer reads them.

The buffer has a fixed number of positions and reuses those
positions when it reaches the end.

The test also records the time taken and the approximate throughput.

The purpose was to understand the basic idea behind ring-buffer
based producer-consumer communication.

This is a basic ring buffer demonstration and not an implementation
of the LMAX Disruptor.

## How to Run

Make sure Java is installed.

Check the Java version:

```bash
java -version
javac -version
```

Compile and run each experiment separately.

### Memory Visibility

```bash
javac MemoryVisibilityTest.java
java MemoryVisibilityTest
```

### CAS vs Lock

```bash
javac CasVsLockBenchmark.java
java CasVsLockBenchmark
```

### False Sharing

```bash
javac FalseSharingTest.java
java FalseSharingTest
```

### Ring Buffer

```bash
javac RingBufferDemo.java
java RingBufferDemo
```

## Evidence

For each experiment, we recorded:

- source code
- execution output
- observed behaviour
- execution time where applicable
- short notes about what we understood

The actual benchmark values can vary depending on the machine and
runtime environment, so the results should be considered observations
from our test environment rather than universal performance numbers.

## What We Learned

The experiments helped us connect the theoretical concepts with
actual multithreaded programs.

The main things we understood were:

- Threads can share data and need proper visibility between them.
- `volatile` can provide visibility guarantees for shared state.
- CAS provides an atomic compare-and-update operation.
- Locks and CAS are different ways of handling concurrent updates.
- False sharing can affect performance even when threads use
  different variables.
- A ring buffer can reuse a fixed amount of memory for
  producer-consumer communication.

The experiments gave us a practical view of the concepts that we
studied in the research note.

## Folder Structure

```text
hardware-concurrency-validation/
│
├── README.md
├── MemoryVisibilityTest.java
├── CasVsLockBenchmark.java
├── FalseSharingTest.java
├── RingBufferDemo.java
│
└── results/
    ├── memory-visibility.txt
    ├── cas-vs-lock.txt
    ├── false-sharing.txt
    └── ring-buffer.txt
```