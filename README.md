# Pliers Mod for Milking Chickens in Minecraft

## Story

Every parent knows that sometimes you have to get creative when answering your child's endless Minecraft questions.

Our son was deep into his Minecraft phase. One summer afternoon, after what felt like hours of Minecraft trivia and stories, my wife decided to have a little fun:

- "Have you heard about chicken-milking pliers?"
- "Chicken-milking pliers?! (WOW)"
- "You craft them almost the same way as shears, and when you use them on a chicken, 3-5 eggs pop out."

The excitement on his face was priceless. We couldn’t resist letting him share this “discovery” with his friends at school. And so, the idea for this mod was born.

Sure, using Forge would have been easier and faster (since it simplifies adding new items), but our server was already running on Bukkit.

## Features

- **Custom Item:** Chicken-milking pliers crafted similarly to shears.

- **Crafting Recipe:**

  ```
  I  
  II
  ```
  Where `I` is an iron ingot.

- **Entity Interactions:**

  - **Chickens:** Drop 3-5 eggs when "milked."

## Technical Details

- **Cooldown:** 3 seconds between uses on the same entity.
- **Durability:** The pliers wear out after 25 uses.
- **Visual Feedback:** Angry villager particles appear when the pliers are on cooldown.

## Build Instructions

To build and deploy the mod:

1. Clone the repository:
   ```
   git clone git@github.com:Dejniel/Minecraft-Pliers-Mod.git
   ```
2. Navigate to the project directory:
   ```
   cd Minecraft-Pliers-Mod/
   ```
3. Build the project using Gradle:
   ```
   ./gradlew build
   ```
4. Deploy the built JAR file to your server:
   ```
   mv lib/build/libs/*.jar /path/to/server/data/plugins/
   ```

## External Link

For more information and downloads, visit the mod's official page:
[Hangar Project Page](https://hangar.papermc.io/Dejniel/Pliers)

---

Have fun exploring Minecraft with the legendary chicken-milking pliers!


