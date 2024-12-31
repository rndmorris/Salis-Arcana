# Commands

Config file: `config/salisarcana/commands.cfg`

Each command can be configured separately from the others, but share common options.
* `aliases` - Alternate, often less-verbose, names that can be used to execute the command.
* `commandEnabled` - Enables or disables the command. Disabled commands are inaccessible in-game.
* `permissionLevel` - The minimum permission level a player must have to execute the command at all.

Some commands may have additional "permission level" config options used to control access to additional functions of
that command. Setting these to a lower permission level than the base `permissionLevel` config option will have
no effect.

## /salisarcana-create-node

<details>

**Description:**

Create a new node at the specified coordinates.

**Usage:**

`/salisarcana-create-node <x> <y> <z> [-t <type>] [-m <modifier>] [--silverwood] [--eerie] [--small] [-a <aspect1> <count1>[ -a <aspect2> <count2>[ ...]]]`

**Arguments:**

|       Argument        | Required? | Details                                                                                                                                                                        |
|:---------------------:|:---------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|     `<x> <y> <z>`     |    Yes    | The coordinates at which to create a new node.                                                                                                                                 |
|      `-t <type>`      |    No     | Specify the type of the new node. If unset, a type will be picked at random.                                                                                                   |
|    `-m <modifier>`    |    No     | Specify the modifier of the new node. If unset, a modifier will be picked at random.                                                                                           |
|    `--silverwood`     |    No     | Spawn a pure, small node, as though generated in a silverwood tree. Mutually exclusive with `--eerie` and `--small`.                                                           |
|       `--eerie`       |    No     | Spawn a dark node, as though generated in an obsidian totem.  Mutually exclusive with `--silverwood`.                                                                          |
|       `--small`       |    No     | Randomly-picked aspects will be low in amount. Mutually exclusive with `--silverwood` and `-a`.                                                                                |
| `-a <aspect> <count>` |    No     | Specify the new node's aspects. Can be included multiple times to specify additional aspects. If not set, aspects will be picked at random. Mutually exclusive with `--small`. |

**Default aliases:**
* `/create-node`

</details>


## /salisarcana-forget-research

<details>

**Description:**

Remove research from a player's knowledge.

**Usage:**

`/salisarcana-forget-research [--all] [--research-key <key> [--research-key <key> [ ...]]] [--player <username>] [--scalpel]`

**Arguments:**

|        Argument        |   Required?   | Details                                                                                                                                                                                  |
|:----------------------:|:-------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|        `--all`         | Conditionally | Remove all research from the player's known research. Required if `research-key` is not provided. Mutually exclusive with `research-key` and `scalpel`.                                  |
| `--research-key <key>` | Conditionally | Specify the id of the research to remove. Required if `--all` is not provided. Can be provided multiple times to specify additional research to remove. Mutually exclusive with `--all`. |
| `--player <username>`  |      No       | Specify the player whose completed research will be altered. If not provided, defaults to the research of the user executing this command.                                               |
|      `--scalpel`       |      No       | If set, remove *only* the specified research. None of its siblings or descendants will be affected. This may have unexpected side effects. Mutually exclusive with `--all`.              |

**Default aliases:**
* `/forget-research`

</details>

## /salisarcana-forget-scanned

<details>

**Description:**
Reset the list of things a player has scanned, allowing those things to be scanned again.

**Usage:**

`/salisarcana-forget-scanned [--player <username>] [--objects] [--entities] [--nodes] [--all]`

**Arguments:**

|       Argument        |   Required    | Details                                                                                                                  |
|:---------------------:|:-------------:|:-------------------------------------------------------------------------------------------------------------------------|
| `--player <username>` |      No       | Specify the player whose scanned things will be altered. If not provided, defaults to the player executing this command. |
|      `--objects`      | Conditionally | Reset scanned items and blocks. Mutually exclusive with `--all`.                                                         |
|     `--entities`      | Conditionally | Reset scanned entities, including mobs. Mutually exclusive with `--all`.                                                 |
|       `--nodes`       | Conditionally | Reset scanned nodes. Mutually exclusive with `--all`.                                                                    |
|        `--all`        | Conditionally | Combine the effects of `--objects`, `--entities`, and `--nodes`. If not set, one of the others is required.              |

**Default aliases:**
* `/forget-scanned`

</details>

## /salisarcana-help

<details>

**Description:**
Get help information about a Salis Arcana command.

**Usage:**

`/salisarcana-help <command>`

**Arguments:**

|  Argument   | Required | Details                                      |
|:-----------:|:--------:|:---------------------------------------------|
| `<command>` |   Yes    | The name or alias of a Salis Arcana command. |

**Default aliases:**
* `/arcana-help`

</details>

## /salisarcana-infusion-symmetry

<details>

**Description:**
Get the symmetry of the nearest runic matrix within 8 blocks, or at the specified coordinates.

**Usage:**
`/salisarcana-infusion-symmetry [<x> <y> <z>]`

**Arguments:**

|   Argument    | Required | Details                                                                |
|:-------------:|:--------:|:-----------------------------------------------------------------------|
| `<x> <y> <z>` |    No    | The coordinates of the Runic Matrix whose stability should be checked. |

**Default aliases:**
* `/infusion-symmetry`

</details>

## /salisarcana-list-research

<details>

**Description:**

List registered Thaumcraft research, grouped by tab in the Thaumonomicon.

**Usage:**

`/salisarcana-list-research [--player <username>] [--search <search term>]`

**Arguments:**

|         Argument         | Required | Details                                                                                                                                                                                    |
|:------------------------:|:--------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|  `--player <username>`   |    No    | Restrict results to only research known by this player.                                                                                                                                    |
| `--search <search term>` |    No    | Restrict results to only research that contains this text in its id or its name. Enclose in double quotation marks to search for a term containing one or more spaces (e.g. "wand craft"). |

**Default aliases:**
* `/list-research`

</details>

## /salisarcana-prereqs

<details>

**Description:**
Lists the prerequisites to unlock a specific research, or the research required to craft a specific item.

**Usage:**
`/salisarcana-prereqs [--research <key> [--completed]] [--item <item-id> [item-damage]]`

**Arguments:**

|             Argument             |   Required    | Details                                                                                                                                                                                                                                |
|:--------------------------------:|:-------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `--research <key> [--completed]` | Conditionally | The unique key of the research to look up, filtered to only those not yet completed. `--completed` will include all research, including any already completed. Required if not setting `--item`, and mutually exclusive with `--item`. |
| `--item <item-id> [item-damage]` | Conditionally | The string id and optional damage value of the item to look up. Mutually exclusive with `--research`.                                                                                                                                  |

**Default aliases:**
* `/prereqs`

</details>

## /salisarcana-update-node

<details>

**Description:**

Update the properties of the node at the specified coordinates.

**Usage:**

`/salisarcana-update-node <x> <y> <z> [-t <type>] [-m <modifier>] [--set <aspect1> <count1>[ --set <aspect2> <count2>[ ...]]] [--rem <aspect3>[ --rem <aspect4>]]`

**Arguments:**

|         Argument         | Required? | Details                                                                                                               |
|:------------------------:|:---------:|:----------------------------------------------------------------------------------------------------------------------|
|      `<x> <y> <z>`       |    Yes    | The coordinates of the node to update.                                                                                |
|       `-t <type>`        |    No     | Specify the node's new type.                                                                                          |
|     `-m <modifier>`      |    No     | Specify the node's new modifier.                                                                                      |
| `--set <aspect> <count>` |    No     | Set how much vis of the specified aspect the node contains. Can be provided multiple times to set additional aspects. |
|    `--rem <aspect>>`     |    No     | Remove an aspect form the node. Can be provided multiple times to remove additional aspects.                          |

**Default aliases:**
* `/update-node`

</details>
