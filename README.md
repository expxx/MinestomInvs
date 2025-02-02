
# Minestom Inventories

Minestom Inventories is a quick and simple inventory management utility for developers.


## Badges

![GitHub License](https://img.shields.io/github/license/expxx/Minestom-Payments?style=for-the-badge)


![Libraries.io dependency status for GitHub repo](https://img.shields.io/librariesio/github/expxx/MinestomInvs?style=for-the-badge)

![GitHub Issues or Pull Requests](https://img.shields.io/github/issues/expxx/MinestomInvs?style=for-the-badge)


## Javadocs
https://expxx.github.io/MinestomInvs/

## Installation

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.expxx</groupId>
    <artifactId>MinestomInvs</artifactId>
    <version>ddd447f336</version>
</dependency>
```

## Usage

```kotlin
val gui: Gui = Gui("id", Component.text("Title"), InventoryType.CHEST_6_ROW)
gui.fillRow(Icon(Material.GRAY_STAINED_GLASS_PANE), 1)
gui.open(player)
```


## Contributing

Contributions are always welcome!

Open an issue/pr :)

