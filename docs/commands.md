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
|    `--retain-warp`     |      No       | If set, any permanent and/or sticky warp associated with the removed research will be **kept**, rather than removed.                                                                     |

**Default aliases:**
* `/forget-research`

</details>

## /salisarcana-forget-scanned

<details>

**Description:**
Reset the list of things a player has scanned, allowing those things to be scanned again.

**Usage:**

`/salisarcana-forget-scanned [--player <username>] [--objects] [--entities] [--nodes] [--all] [--aspects <...>] [--all-aspects] [--hand] [--inventory] [--looking] [--container] `

**Arguments:**

|                  Argument                  |   Required    | Details                                                                                                                  |
|:------------------------------------------:|:-------------:|:-------------------------------------------------------------------------------------------------------------------------|
|           `--player <username>`            |      No       | Specify the player whose scanned things will be altered. If not provided, defaults to the player executing this command. |
|                `--objects`                 | Conditionally | Reset scanned items and blocks. Mutually exclusive with `--all`.                                                         |
|                `--entities`                | Conditionally | Reset scanned entities, including mobs. Mutually exclusive with `--all`.                                                 |
|                 `--nodes`                  | Conditionally | Reset scanned nodes. Mutually exclusive with `--all`.                                                                    |
|                  `--all`                   | Conditionally | Combine the effects of `--objects`, `--entities`, and `--nodes`. If not set, one of the others is required.              |
|                  `--hand`                  | Conditionally | Reset the item currently held. Mutually exclusive with `--all`.                                                          |
|               `--inventory`                | Conditionally | Resets all items in your inventory.  Mutually exclusive with `--all`.                                                    |
|                `--looking`                 | Conditionally | Resets the block you are looking at. Mutually exclusive with `--all`.                                                    |
|               `--container`                | Conditionally | Resets all items inside of the container (eg. chest) you are looking at. Mutually exclusive with `--all`.                |


**Default aliases:**
* `/forget-scanned`

</details>

## /salisarcana-forget-aspect

<details>

**Description:**
Forget or reset the amount of a specific aspect a player has scanned.

**Usage:**

`/salisarcana-forget-aspect [--player <username>] [--reset] [--forget] [--aspect <...>] [--all]`

**Arguments:**

|       Argument        |   Required    | Details                                                                                                           |
|:---------------------:|:-------------:|:------------------------------------------------------------------------------------------------------------------|
| `--player <username>` |      No       | Specify the player whose aspects will be altered. If not provided, defaults to the player executing this command. |
|       `--reset`       | Conditionally | Resets the current research point count to 1. Mutually exclusive with `--forget`.                                 |
|      `--forget`       | Conditionally | Forgets the aspect entirely, requiring it to be relearned. Mutually exclusive with `--reset`.                     |
|        `--all`        | Conditionally | Modify all aspects. Mutually exclusive with `--aspect`                                                            |
|   `--aspect <...>`    | Conditionally | Modify a subset of aspects. Mutually exclusive with `--all`.                                                      |


**Default aliases:**
* `/forget-aspect`

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

## /salisarcana-upgrade-focus

<details>

**Description:**

Add focus upgrades to a wand focus held in your or another player's hand. Will not apply upgrades beyond the focus's five-upgrade limit.

**Usage:**

`/salisarcana-upgrade-focus <upgrade[ upgrade[ ...]]> [--player <username>]`

**Arguments:**

|            Argument             | Required? | Details                                                                                                                                            |
|:-------------------------------:|:---------:|:---------------------------------------------------------------------------------------------------------------------------------------------------|
| `<upgrade [upgrade[ upgrade]]>` |    Yes    | A list of one to five upgrade ids to be applied. Use tab-completion, or see the table below (vanilla Thaumcraft only), to view available upgrades. |
|      `--player <username>`      |    No     | If set, apply upgrades to the specified player's held focus instead of your own.                                                                   |

The `upgrade` argument is presented in the form `defaultLocalizedName-internalId`. The upgrades available as part of base Thaumcraft are
presented below. Upgrades added by Thaumcraft addons should be automatically supported by the command.

|      Upgrade      | Internal Id | Expected Argument  |
|:-----------------:|:-----------:|:-------------------|
|      Potency      |      0      | potency-0          |
|      Frugal       |      1      | frugal-1           |
|     Treasure      |      2      | treasure-2         |
|      Enlarge      |      3      | enlarge-3          |
| Alchemist's Fire  |      4      | alchemists-fire-4  |
| Alchemist's Frost |      5      | alchemists-frost-5 |
|     Architect     |      6      | architect-6        |
|      Extend       |      7      | extend-7           |
|    Silk Touch     |      8      | silk-touch-8       |
|     Fireball      |      9      | fireball-9         |
|     Fire Beam     |     10      | fire-beam-10       |
|    Scattershot    |     11      | scattershot-11     |
|    Ice Boulder    |     12      | ice-boulder-12     |
|     Bat Bombs     |     13      | bat-bombs-13       |
|    Devil Bats     |     14      | devil-bats-14      |
|    Nightshade     |     15      | nightshade-15      |
|      Seeker       |     16      | seeker-16          |
|  Chain Lightning  |     17      | chain-lightning-17 |
|    Earth Shock    |     18      | earth-shock-18     |
|   Vampire Bats    |     19      | vampire-bats-19    |
|      Dowsing      |     20      | dowsing-20         |

**Default aliases:**
* `/upgrade-focus`

</details>
