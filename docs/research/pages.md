# Supported Page Types

Salis Arcana currently supports the following page types:

- [Arcane Crafting](#arcane-crafting)
- [Aspect](#aspect)
- [Crucible](#crucible)
- [Image](#image)
- [Infusion Crafting](#infusion-crafting)
- [Normal Crafting](#normal-crafting)
- [Smelting](#smelting)
- [Text](#text)

Each page type has its own unique properties and requirements. Below is a brief overview of each page type.

## Arcane Crafting

```json
{
  "type": "arcane",
  "number": <pagenumber>,
  "item": {
    "item": "modid:item", // this is the recipe's output item
    "meta": <meta>,
    "amount": <amount>
  }
}
```

## Aspect

```json
{
  "type": "aspect",
  "number": <pagenumber>,
  "aspect": [
    {
      "name": "aspectname",
      "amount": <amount>
    },
    {
      "name": "aspectname",
      "amount": <amount>
    }
  ]
}
```

## Crucible

```json
{
  "type": "crucible",
  "number": <pagenumber>,
  "item": {
    "item": "modid:item", // this is the recipe's output item
    "meta": <meta>,
    "amount": <amount>
  }
}
```

## Image

```json
{
  "type": "image",
  "number": <pagenumber>,
  "text": "<description>",
  "resource": "/path/to/resource"
}
```

## Infusion Crafting

```json
{
  "type": "infusion",
  "number": <pagenumber>,
  "item": {
    "item": "modid:item", // this is the recipe's output item
    "meta": <meta>,
    "amount": <amount>
  }
}
```

## Normal Crafting

```json
{
  "type": "crafting",
  "number": <pagenumber>,
  "item": {
    "item": "modid:item", // this is the recipe's output item
    "meta": <meta>,
    "amount": <amount>
  }
}
```

## Smelting

```json
{
  "type": "smelting",
  "number": <pagenumber>,
  "item": {
    "item": "modid:item", // this is the recipe's output item
    "meta": <meta>,
    "amount": <amount>
  }
}
```

## Text

```json
{
  "type": "text",
  "number": <pagenumber>,
  "text": "<description>"
}
```
