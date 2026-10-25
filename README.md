# User Stories - Hackcraft Minecraft Mod

The aim of this project is to submit a Minecraft mod as part of Hackcraft 26/27, organised by Hackclub. The following user stories describe the mod and its desired features in more detail.

## 1. Version Control mit Git und GitHub

As a developer,
I want to be able to manage my code without having to keep creating manual copies of my project. I want to be able to access previous versions of my project in the event of errors or changes, and to work from there. Should this project become a team effort, I want to be able to work with my team members without us getting in each other’s way or hindering one another.

Acceptance criteria
- All team members use Git
- A repository exists on GitHub, that is a fork of the example-mod repository **https://github.com/FabricMC/fabric-example-mod**
- Every team member has a clone of the repository
- The work is split on different dedicated branches
- Team members switch between branches for different tasks
- In the final project necessary branches are merged oder rebased and all conflicts resolved
- All dead branches are deleted

## 2. Redstone Funktion Converter

As a redstone engineer,
I don’t want to waste valuable space on complex Redstone functions with large, clunky constructions. I want a simple Redstone component that outputs a signal based on 1–3 input signals. I want to be able to customise this function by providing examples such as (3, 4, 15) → 6, and the rest of the function should be estimated automatically.

Acceptance criteria
- There are block entities named "Signal Converter"
- There is a block model und textures
- They have three clearly marked inputs
- They have a clearly marked output
- The I/O- Interfaces read or write redstone signals
- There is a GUI, where the user is able to enter input values and expected values into a table, that is saved on close and loaded on opening
- An ANN uses the saved data as a reference and estimates through the training process on the daten the required function
- The ANN is saved on close and loaded on opening per Block Entity

## 3. Redstone Security Module

As a wealthy (in-game) player,
I want to protect my boundless riches from greedy players. However, simple Redstone passwords with a strength of 2^n aren’t secure enough for me and take up a lot of space in my vault. I’d like a sort of combination lock that can be built without taking up too much space but makes it significantly harder to carry out a brute-force attack.

Acceptance criteria
- There are block entities named "Security Module"
- There are a block model und textures
- There is an output sending redstone signals, on passing verification, however there is no feedback from the block entity, whether the input is correct.
- The owner of the block entities is able to change the correct number in a GUI
- In the GUI the user is able to set the duration of the signal in ticks
- The input takes the form of a number drawn freehand
- A CNN trained on MNIST data is tasked to classify the input drawings
- The CNN is trained in python and the parameters are uploaded to the CNN of the Mod after finishing the training
- The CNNs accuracy is at least at 98%

## 4. Drawing Game

As a player who’s chronically bored,
who sometimes finds Minecraft a bit too taxing or just needs a break to clear my head, I’d like to have a little casual game that I can play either on my own or in multiplayer with friends without having to switch to another game.

Acceptance criteria
- The Game has a GUI. The Game starts by the player issuing a command, with up to 6 players
- Using a command parameter the games duration can be set between 3 - 6 rounds
- The GUI shows a list of participating players
- The GUI features a drawing area where the active player can draw the given word. The other players can try to guess the word using a text input field. The quicker they manage to do so, the more points they and the player drawing the word receive.
- Both offline and online, a bot is part of the game
- The bot consists of a pretrained model enhanced using Extraction on training the images the players draw. Like a regular player the bot receives points for guessing correctly
- The bot collects a dataset from all the drawings with the labels and saves them for training purposes
