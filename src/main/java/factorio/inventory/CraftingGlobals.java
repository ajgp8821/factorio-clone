package factorio.inventory;

import factorio.GameState;
import factorio.TechLevel;

import java.util.ArrayList;

public class CraftingGlobals {

    // What you can make a workbench given tech level
    private CraftingRecipe[] starterCrafts;
    private CraftingRecipe[] workbenchCrafts;
    private CraftingRecipe[] kilnCrafts;
    private CraftingRecipe[] forgeCrafts;

    // Equipment is weird because they're unlocked but also 1-time crafts
    private CraftingRecipe[] equipmentCrafts;
    private TechLevel[] equipmentUnlocks;

    // Probably need a new recipe class (CraftingSmelt)
    // to take fuel into account
    private CraftingRecipe[] kilnSmelts;
    private CraftingRecipe[] forgeSmelts;

    public CraftingGlobals () {
        initializeRecipes();
    }

    // Equipment should only be craftable at a workbench, NOT an autoworkbench
    // hence the parameter
    public CraftingRecipe[] getWorkbenchCrafts (boolean includeEquipment) {
        ArrayList<CraftingRecipe> recipes = new ArrayList<CraftingRecipe>();

        for (CraftingRecipe recipe : starterCrafts)
            recipes.add(recipe);

        // By being at a workbench, it's guaranteed that tech level is at least WORKBENCH, so no need for ifs until after
        for (CraftingRecipe recipe : workbenchCrafts)
            recipes.add(recipe);

        int currentTechOrdinal = GameState.techLevel.ordinal();
        if (currentTechOrdinal >= TechLevel.KILN.ordinal()) {
            for (CraftingRecipe recipe : kilnCrafts)
                recipes.add(recipe);
        }

        if (currentTechOrdinal >= TechLevel.FORGE.ordinal()) {
            for (CraftingRecipe recipe : forgeCrafts)
                recipes.add(recipe);
        }

        if (includeEquipment) {
            for (int i=0; i<equipmentCrafts.length; i++) {
                int requiredLevel = equipmentUnlocks[i].ordinal();
                boolean alreadyCrafted = GameState.player.getInventory().getQuantity(equipmentCrafts[i].result()) > 0;
                if (currentTechOrdinal >= requiredLevel &&
                        alreadyCrafted == false)
                    recipes.add(equipmentCrafts[i]);
            }
        }

        // Conversion
        CraftingRecipe[] returnRecipes = new CraftingRecipe[recipes.size()];
        for (int i=0; i<returnRecipes.length; i++) {
            returnRecipes[i] = recipes.get(i);
        }

        return returnRecipes;
    }

    public CraftingRecipe[] getPlayerCrafts () {
        return starterCrafts;
    }

    public CraftingRecipe[] getKilnCrafts () {
        return kilnSmelts;
    }

    public CraftingRecipe[] getForgeCrafts () {
        return forgeSmelts;
    }





    //
    // Recipe decleration
    //

    private void initializeRecipes () {
        starterCrafts = new CraftingRecipe[] {
                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.STONE},
                        new int[]       {5},
                        ItemIndex.WORKBENCH,
                        "Unlocks more crafting options"),

        };

        workbenchCrafts = new CraftingRecipe[] {
                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE},
                        new int[]       {2,              4},
                        ItemIndex.MINING_DRILL,
                        "Extract infinite resources from a single tile"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE},
                        new int[]       {4,              5},
                        ItemIndex.CHEST,
                        "Can store items"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE},
                        new int[]       {1,              2},
                        ItemIndex.LANDFILL,
                        "Fills water in with dirt that can be walked & built on top of"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE},
                        new int[]       {2,              10},
                        ItemIndex.KILN,
                        "Process copper & sand to produce new resources"),
        };

        // TODO: Crafts are too hard, there are 2 hard crafts: the forge & the sub
        // It might be good to remove mining drills from start

        //
        // These next two arrays are made at the workbench, but only after
        // having unlocked the kiln or forge
        kilnCrafts = new CraftingRecipe[] {
                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.STONE, ItemIndex.BAR_COPPER},
                        new int[]       {5,               5},
                        ItemIndex.AUTO_MINING_UPGRADE,
                        "Placed onto a mining drill, will automatically collect resources"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.STONE},
                        new int[]       {5},
                        ItemIndex.ITEM_TUBE_STONE,
                        "Transports items between tubes & inserts into inventories"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.GLASS},
                        new int[]       {5},
                        ItemIndex.ITEM_TUBE_GLASS,
                        "Pulls items out of inventories"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.STONE, ItemIndex.BAR_COPPER, ItemIndex.GLASS},
                        new int[]       {15,              15,                   15},
                        ItemIndex.FORGE,
                        "Smelt iron and produce alloys"),
        };

        forgeCrafts = new CraftingRecipe[] {

                // TODO: GLASS Here
                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.WORKBENCH, ItemIndex.BAR_COPPER},
                        new int[]       {1,                   10},
                        ItemIndex.COPPER_WORKBENCH,
                        "Will automatically craft when a recipe is selected and items are pumped in"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.KILN, ItemIndex.BAR_IRON},
                        new int[]       {1,              10},
                        ItemIndex.IRON_KILN,
                        "Can smelt like a kiln, but automatically"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.FORGE, ItemIndex.ALLOY_STEEL},
                        new int[]       {1,               10},
                        ItemIndex.STEEL_FORGE,
                        "Can smelt like a forge, but automatically"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.ALLOY_STEEL},
                        new int[]       {5},
                        ItemIndex.ITEM_TUBE_STEEL,
                        "Used to filter out-going items"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.STONE, ItemIndex.GLASS, ItemIndex.ALLOY_STEEL},
                        new int[]       {20,              30,              40},
                        ItemIndex.SUBMARINE,
                        "The ultimate creation"),
        };



        //
        // Equipment is weird because it's a 1-time craft
        equipmentCrafts = new CraftingRecipe[] {
                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.WOOD, ItemIndex.ALLOY_BRONZE},
                        new int[]       {20,             25},
                        ItemIndex.BOAT,
                        "Move across water without landfills"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.BAR_IRON, ItemIndex.ALLOY_BRONZE},
                        new int[]       {10,                 5},
                        ItemIndex.TALL_BOOTS,
                        "Improved mobility over factory components"),
        };

        // Stores at what TechLevel each equipment is unlocked
        equipmentUnlocks = new TechLevel[] {
                TechLevel.WORKBENCH,  // Pickaxe
                TechLevel.FORGE, // Boat
                TechLevel.KILN,  // Shovel
                TechLevel.FORGE, // Tall Boots
        };



        //
        // These are made at the Kiln & Forge
        kilnSmelts = new CraftingRecipe[] {
                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.ORE_COPPER, ItemIndex.ORE_COAL},
                        new int[]       {2,             1},
                        ItemIndex.BAR_COPPER,
                        "Can be used to craft more items"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.SAND, ItemIndex.ORE_COAL},
                        new int[]       {3,             1},
                        ItemIndex.GLASS,
                        "Can be used to craft more items"),
        };

        forgeSmelts = new CraftingRecipe[] {
                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.ORE_IRON, ItemIndex.ORE_COAL},
                        new int[]       {2,             1},
                        ItemIndex.BAR_IRON,
                        "Can be used to craft more items"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.BAR_COPPER, ItemIndex.BAR_IRON, ItemIndex.ORE_COAL},
                        new int[]       {1,                   2,                  1},
                        ItemIndex.ALLOY_BRONZE,
                        "Can be used to craft more items"),

                new CraftingRecipe(
                        new ItemIndex[] {ItemIndex.BAR_IRON, ItemIndex.ORE_COAL},
                        new int[]       {3,                  2},
                        ItemIndex.ALLOY_STEEL,
                        "Can be used to craft more items"),
        };
    }

}
