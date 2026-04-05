<p align="center"><img src="./src/main/resources/icon.png" alt="Logo" width="200"></p>
<h1 align="center">Priklen’s Machines</h1>

*Pricklen’s Machines* is a small mod that adds a bunch (only one for now) of machines, that bring new ways of crafting to Minecraft!

The mod was originally developed for the upcoming "Dieselpunk" modpack, but was later released as a standalone project.

### [KubeJS](https://github.com/KubeJS-Mods/KubeJS) Integration
```javascript
ServerEvents.recipes(event => {
    // minecraft:redstone -> minecraft:glowstone in 5 seconds
    event.recipes.pricklensmachines.kiln_smelting("minecraft:glowstone", ["minecraft:redstone"], 100);
})
```

---

This mod depends on:
- Flywheel (MIT License)
- Ponder (MIT License)