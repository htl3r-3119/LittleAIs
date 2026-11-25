# Little AIs
## User Stories - Hackcraft Minecraft Mod

The aim of this project is to submit a Minecraft mod as part of Hackcraft 26/27, organised by Hackclub. The following user stories describe the mod and its desired features in more detail.

## 1. Version Control mit Git und GitHub

As a developer,
I want to be able to manage my code without having to keep creating manual copies of my project. I want to be able to access previous versions of my project in the event of errors or changes, and to work from there. Should this project become a team effort, I want to be able to work with my team members without us getting in each other’s way or hindering one another.

Acceptance criteria
- All team members use Git
- A repository exists on GitHub, that is a fork of the example-mod repository **https://github.com/FabricMC/fabric-example-mod**
- Every team member has a clone of the repository
- The work is split on different dedicated branches
- Team members can switch between branches for different tasks
- In the final project necessary branches are merged oder rebased and all conflicts resolved
- All dead branches are deleted

## 2. Redstone Funktion Converter

As a redstone engineer,
I don’t want to waste valuable space on complex Redstone functions with large, clunky constructions. I want a simple Redstone component that outputs a signal based on 1–3 input signals. I want to be able to customise this function by providing examples such as (3, 4, 15) → 6, and the rest of the function should be estimated automatically.

Acceptance criteria
- There are block entities named e.g "Signal Converter"
- There is a block model und textures
- They have three clearly marked inputs
- They have a clearly marked output
- The I/O- Interfaces read or write redstone signals
- There is a GUI, where the user is able to enter input values and expected values into a table, that is saved on close and loaded on opening
- An ANN uses the saved data as a reference and estimates through the training process on the daten the required function
- The ANN is saved on close and loaded on opening per Block Entity

## 3. Redstone Security Module

As a wealthy (in-game) player,
I want to protect my boundless riches from greedy players. However, simple Redstone passwords with a strength of 2$^n$ aren’t secure enough for me and take up a lot of space in my vault. I’d like a sort of combination lock that can be built without taking up too much space but makes it significantly harder to carry out a brute-force attack.

Acceptance criteria
- There are block entities named e.g "Security Module"
- There are a block model und textures
- There is an output sending redstone signals, on passing verification, however there is no feedback from the block entity, whether the input is correct.
- The owner of the block entities is able to change the correct number in a GUI
- In the GUI the user is able to set the duration of the signal in ticks
- The input takes the form of a number drawn freehand
- A CNN trained on MNIST data is tasked to classify the input drawings
- The CNN is trained in python and the parameters are uploaded to the CNN of the Mod after finishing the training
- The CNNs accuracy is at least at 98%
