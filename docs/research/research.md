# Research Editor

Salis Arcana includes a JSON-based research creator and editor, allowing you to create new researches and edit pre-existing ones.

Any new or edited researches should be placed in `config/salis_arcana/researches/` as a JSON file.

## Creating a Research

An example JSON file for defining a research is included in [the documentation](./research.md).

## Editing a Research

Run the command `/salisarcana-dump-research --research <researchkey>` to dump the JSON for the research into `config/salisarcana/research/<researchkey>.json`.

From there, any changes you make to the JSON file will be reflected in-game.

