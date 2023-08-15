[![CodeFactor](https://www.codefactor.io/repository/github/zorbeytorunoglu/thebestranks/badge)](https://www.codefactor.io/repository/github/zorbeytorunoglu/thebestranks)

# What is TheBestRanks (TBR)?

TheBestRanks (TBR) is an easy to use ranking Spigot plugin for Minecraft that makes having unlimited ranks for players to climb quite easy. You can create as much requirement as you like for a rank with PlaceholderAPI placeholders. It accepts "string, int, double, float, long" values as return.

There is also a menu that is automatically created by the plugin which allows players to see how many ranks they have passed so far, their current rank and how many more there are to go for.

# Installition

1. Download thebestranks and move it into your "plugins" folder
2. Download [kLib](https://www.spigotmc.org/resources/klib-spigot-kotlin-api-lib-to-make-coding-in-kotlin-easier-better.107471/) and move it into your "plugins" folder
3. Download [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) and move it into your "plugins" folder
4. Start and stop your server to start configuring your ranks.yml

Important: Don't forget to download the related expansions of the PlaceholderAPI for the placeholders that you use as requirements in ranks. Otherwise, they won't work.

For example: If you use "%player_level%" placeholder in a rank as a requirement, you need to have "Player" expansion downloaded and loaded which is really easy to do: "/papi ecloud download player" and "/papi reload". You don't need to do this for all the placeholders that you are going to use. It is just for the placeholders in this list: [placeholders](https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders)

You can use RGB colors whenever you like. If your server's version is 1.16+, of course.
# Configuring
First of all, ranks are some levels that you can achieve when you meet all the requirements. It is also good to remind that you may not have any requirements for a rank. If you don't have requirements for a rank, it would be free to achieve.

### Rank
Ranks needs to have a prefix and a display name. All the other configurations are actually optional but it wouldn't make sense for you to use a rank plugin if you are not going to use the requirements.

Example rank configuration:

![image](https://github.com/zorbeytorunoglu/thebestranks/assets/6390409/404fa9e6-86c4-4e62-9696-a7790909338a)

This is the rank that all the new beginners in your server will start from. You don't need to specify any requirements or any other configurations that we will do for others.

### Prefix
Prefix is just a String that will be returned when %thebestranks_rank_prefix% is called. Supports RGB.

### Display Name
Display name is just a String that will be returned when %thebestranks_rank_displayname% is called. Supports RGB.

### Lore
If this rank is some player's "in progress" rank, the lore you will put here will be shown in his menu.

### Requirements

You can use _any PlaceholderAPI placeholder_ as a requirement. There are few steps you need to remember though.

### types

Available types:
- STRING (texts)
- INT (numbers)
- DOUBLE (double-precision numbers)
- FLOAT
- LONG

If your placeholders return a text; use **STRING**. If your placeholder returns numbers without decimals; use **INT**. If your placeholder returns double-precision numbers like money balances; use **DOUBLE**. If you don't know what are those last types, you probably won't need to use them.

For example:

If you want to use **%player_item_in_hand%** placeholder as a requirement, you need to use **STRING** type because it returns the material's name as _text_.

Like this:

![image](https://github.com/zorbeytorunoglu/thebestranks/assets/6390409/5e2469cf-5677-4c7c-ab6f-ed361477acbb)

### cases

Cases are quite easy.

If you use %player_level% as requirement placeholder and use "equals or greater", player will meet the requirement of his level is equals or greater than the value.
If you use %player_item_in_hand% as requirement placeholder and use "not equals", requirement will be met if player holds anything except for the value material.

Available cases:
- equals (for ALL)
- not equals (for ALL)
- equals or greater (for INT, DOUBLE, LONG, FLOAT)
- equals or lesser (for INT, DOUBLE, LONG, FLOAT)
- greater (for INT, DOUBLE, LONG, FLOAT)
- lesser (for INT, DOUBLE, LONG, FLOAT)

### Requirement placeholders in lore
**%requirement_X%**: is a requirement placeholder. For example, if you have 2 requirements for that rank, you can use %requirement_0% and %requirement_1% in lore as placeholder. Don't forget to start from 0. If you want to refer to the first requirement, use 0, if you want to refer the second requirement, use 1, if you want to refer the third requirement use 2... it can go forever. Just understand the logic.

### Requirement: gui_message
They will replace %requirement_X% placeholders that was mentioned above when the rank is an in-progress rank for a player.
**%your%**: will replace the player's placeholder. For example, if you used _%player_level%_ as requirement, %your% placeholder in lore will be replaced by the level of the player.
**%status%**: will be replaced by the status messages in messages.yml. For the default config, it returns a _check mark_ if requirement is met, an _X_ if it is not met.

### Requirement: deny_message
If the requirement isn't met yet and player tries to rank up, this message will be sent to him to notify. 

### Commands
Commands will be executed by console if a player ranks up. You can reward, punish, re-arrange them if you like.

# Placeholders (PlaceholderAPI)

- %thebestranks_rank_prefix% - Returns the prefix of the player's rank
- %thebestranks_rank_displayname% - Returns the display name of the player's rank
- %thebestranks_rank_order% - Returns the order of the player's rank.

They can be used in any plugin that supports PlaceholderAPI.

# Commands

- /rank
- /rank up
- /rank help
- /rank set <player> <rank>
- /rank check <player>
- /rank reload

# Permissions

- thebestranks.admin - Admin permission
- thebestranks.set - "/rank set" permission
- thebestranks.check - "/rank check" permission
- thebestranks.rank - "/rank" permission
- thebestranks.rankup - "/rank up" permission

# API

## Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.zorbeytorunoglu</groupId>
        <artifactId>thebestranks</artifactId>
        <version>{LATEST_RELEASE_HERE}</version>
    </dependency>
</dependencies>
```

## Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    implementation 'com.github.zorbeytorunoglu:thebestranks:LASTEST_RELEASE_HERE'
}
```

## Access to TBRAPI

![image](https://github.com/zorbeytorunoglu/thebestranks/assets/6390409/a954642f-d142-4960-98ae-8e7fac785ecd)

We also have a custom event that is being triggered when a player ranks up "**RankUpEvent**".

# FAQ

Check your logs. You will probably see the problem there.
## My menu is not loading
You are probably using items that does not exist in your Minecraft version.

## My rank requirement is not working
You are probably not downloading the expansion. try "/papi ecloud download <expansion>". For example, if you are making a requirement with player expansion (%player_level%) you need to submit "/papi ecloud download player" and then "/papi reload".
