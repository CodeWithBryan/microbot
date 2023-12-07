package net.runelite.client.plugins.microbot.util.gameobject;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.cache.ObjectManager;
import net.runelite.cache.fs.Store;
import net.runelite.client.RuneLite;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.util.math.Calculations;
import net.runelite.client.plugins.microbot.util.math.ObjectStrategy;
import net.runelite.client.plugins.microbot.util.math.Random;
import net.runelite.client.plugins.microbot.util.reflection.Rs2Reflection;
import net.runelite.client.rs.ClientLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static net.runelite.client.plugins.microbot.util.Global.sleep;


public class Rs2GameObject {
    private static Store store;
    private static ObjectManager manager;

    public static ObjectManager getManager() throws IOException {
        if (manager == null) {
            store = new Store(new File(RuneLite.RUNELITE_DIR, "jagexcache/oldschool/LIVE"));
            store.load();
            manager = new ObjectManager(store);
            manager.load();
        }
        return manager;
    }

    public static TileObject objectToInteract = null;
    public static String objectAction = null;

    public static boolean interact(WorldPoint worldPoint) {
        TileObject gameObject = findObjectByLocation(worldPoint);
        return clickObject(gameObject);
    }

    public static boolean interact(GameObject gameObject) {
        return clickObject(gameObject);
    }

    public static boolean interact(TileObject tileObject) {
        return clickObject(tileObject, null);
    }

    public static boolean interact(TileObject tileObject, String action) {
        return clickObject(tileObject, action);
    }

    public static boolean interact(GameObject gameObject, String action) {
        return clickObject(gameObject, action);
    }

    public static boolean interact(int id) {
        TileObject object = findObjectById(id);
        return clickObject(object);
    }

    public static boolean interact(int id, String action) {
        TileObject object = findObjectById(id);
        return clickObject(object, action);
    }

    public static boolean interact(int id, String action, int distance) {
        TileObject object = findObjectByIdAndDistance(id, distance);
        return clickObject(object, action);
    }

    public static boolean interact(String name, String action) {
        TileObject object = findObject(name);
        return clickObject(object, action);
    }

    public static TileObject interactAndGetObject(int id) {
        TileObject object = findObjectById(id);
        clickObject(object);
        return object;
    }

    public static boolean interact(int[] objectIds, String action) {
        for (int objectId : objectIds) {
            if (interact(objectId, action)) return true;
        }
        return false;
    }

    public static boolean interact(String objectName) {
        GameObject object = findObject(objectName, true);
        return clickObject(object);
    }

    public static boolean interactByOptionName(String action) {
        GameObject object = findObjectByOption(action);
        return clickObject(object);
    }

    public static boolean interact(String objectName, boolean exact) {
        GameObject object = findObject(objectName, exact);
        return clickObject(object);
    }

    public static boolean interact(String objectName, String action, boolean exact) {
        GameObject object = findObject(objectName, exact);
        return clickObject(object, action);
    }

    public static GameObject findObject(String objectName) {
        return findObject(objectName, true);
    }

    public static ObjectComposition findObject(int id) {
        return Microbot.getClientThread().runOnClientThread(() -> Microbot.getClient().getObjectDefinition(id));
    }

    public static boolean exists(int id) {
        return findObjectById(id) != null;
    }

    public static TileObject findObjectById(int id) {

        List<GameObject> gameObjects = getGameObjects();

        if (gameObjects == null) return null;

        for (net.runelite.api.GameObject gameObject : gameObjects) {
            if (gameObject.getId() == id)
                return gameObject;
        }

        List<GroundObject> groundObjects = getGroundObjects();

        for (GroundObject groundObject : groundObjects) {
            if (groundObject.getId() == id)
                return groundObject;
        }

        List<WallObject> wallObjects = getWallObjects();


        for (WallObject wallObject : wallObjects) {
            if (wallObject.getId() == id)
                return wallObject;
        }

        List<DecorativeObject> decorationObjects = getDecorationObjects();


        for (DecorativeObject decorativeObject : decorationObjects) {
            if (decorativeObject.getId() == id)
                return decorativeObject;
        }

        return null;
    }

    public static TileObject findObjectByLocation(WorldPoint worldPoint) {

        List<GameObject> gameObjects = getGameObjects();

        if (gameObjects == null) return null;

        for (net.runelite.api.GameObject gameObject : gameObjects) {
            if (gameObject.getWorldLocation().equals(worldPoint))
                return gameObject;
        }

        List<GroundObject> groundObjects = getGroundObjects();

        for (GroundObject groundObject : groundObjects) {
            if (groundObject.getWorldLocation().equals(worldPoint))
                return groundObject;
        }

        List<WallObject> wallObjects = getWallObjects();


        for (WallObject wallObject : wallObjects) {
            if (wallObject.getWorldLocation().equals(worldPoint))
                return wallObject;
        }

        List<DecorativeObject> decorationObjects = getDecorationObjects();


        for (DecorativeObject decorativeObject : decorationObjects) {
            if (decorativeObject.getWorldLocation().equals(worldPoint))
                return decorativeObject;
        }

        return null;
    }

    public static TileObject findObjectByIdAndDistance(int id, int distance) {

        List<GameObject> gameObjects = getGameObjects();

        gameObjects = gameObjects.stream()
                .filter(x -> Microbot.getClient().getLocalPlayer().getWorldLocation().distanceTo(x.getWorldLocation()) < distance)
                .collect(Collectors.toList());

        for (net.runelite.api.GameObject gameObject : gameObjects) {
            if (gameObject.getId() == id)
                return gameObject;
        }

        List<GroundObject> groundObjects = getGroundObjects();

        groundObjects = groundObjects.stream().filter(x -> Microbot.getClient().getLocalPlayer().getWorldLocation().distanceTo(x.getWorldLocation()) < distance).collect(Collectors.toList());


        for (GroundObject groundObject : groundObjects) {
            if (groundObject.getId() == id)
                return groundObject;
        }

        List<WallObject> wallObjects = getWallObjects();

        wallObjects = wallObjects.stream().filter(x -> Microbot.getClient().getLocalPlayer().getWorldLocation().distanceTo(x.getWorldLocation()) < distance).collect(Collectors.toList());


        for (WallObject wallObject : wallObjects) {
            if (wallObject.getId() == id)
                return wallObject;
        }

        List<DecorativeObject> decorationObjects = getDecorationObjects();

        decorationObjects = decorationObjects.stream().filter(x -> Microbot.getClient().getLocalPlayer().getWorldLocation().distanceTo(x.getWorldLocation()) < distance).collect(Collectors.toList());

        for (DecorativeObject decorativeObject : decorationObjects) {
            if (decorativeObject.getId() == id)
                return decorativeObject;
        }

        return null;
    }

    private static List<DecorativeObject> getDecorationObjects() {
        Scene scene = Microbot.getClient().getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = Microbot.getClient().getPlane();
        List<DecorativeObject> tileObjects = new ArrayList<>();
        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    continue;
                }

                tileObjects.add(tile.getDecorativeObject());
            }
        }


        return Arrays.stream(tileObjects.toArray(new DecorativeObject[tileObjects.size()]))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(value -> value.getLocalLocation().distanceTo(Microbot.getClient().getLocalPlayer().getLocalLocation())))
                .collect(Collectors.toList());
    }

    public static GameObject findObjectById(int id, int x) {

        List<GameObject> gameObjects = getGameObjects();

        if (gameObjects == null) return null;

        for (net.runelite.api.GameObject gameObject : gameObjects) {
            if (gameObject.getId() == id && gameObject.getWorldLocation().getX() == x)
                return gameObject;
        }

        return null;
    }

    public static GameObject findObject(int id, WorldPoint worldPoint) {

        List<GameObject> gameObjects = getGameObjects();

        if (gameObjects == null) return null;

        for (net.runelite.api.GameObject gameObject : gameObjects) {
            if (gameObject.getId() == id && gameObject.getWorldLocation().equals(worldPoint))
                return gameObject;
        }

        return null;
    }

    public static ObjectComposition findObjectComposition(int id) {

        List<GameObject> gameObjects = getGameObjects();

        if (gameObjects == null) return null;

        for (net.runelite.api.GameObject gameObject : gameObjects) {
            if (gameObject.getId() == id) {
                return convertGameObjectToObjectComposition(gameObject);
            }
        }
        return null;
    }


    public static GameObject findObject(String objectName, boolean exact) {
        List<GameObject> gameObjects = getGameObjects();

        if (gameObjects == null) {
            return null;
        }

        return Microbot.getClientThread().runOnClientThread(() -> {
            for (GameObject gameObject : gameObjects) {
                ObjectComposition objComp = convertGameObjectToObjectCompositionOnClientThread(gameObject);

                if (objComp == null) {
                    continue;
                }
                String compName = null;

                try {
                    compName = !objComp.getName().equals("null") ? objComp.getName() : (objComp.getImpostor() != null ? objComp.getImpostor().getName() : null);
                } catch (Exception e) {
                    continue;
                }

                if (!exact && compName.toLowerCase().contains(objectName.toLowerCase())) {
                    return Microbot.getWalker().canInteract(gameObject.getWorldLocation()) ? gameObject : null;
                } else if (exact && compName.equalsIgnoreCase(objectName)) {
                    return Microbot.getWalker().canInteract(gameObject.getWorldLocation()) ? gameObject : null;
                }
            }
            return null;
        });
    }

    public static GameObject getObject(int objectId) {
        List<GameObject> gameObjects = getGameObjects();

        if (gameObjects == null) {
            return null;
        }

        return Microbot.getClientThread().runOnClientThread(() -> {
            for (GameObject gameObject : gameObjects) {
                if (gameObject.getId() == objectId)
                    return Calculations.pathLengthTo(new ObjectStrategy(gameObject, getManager().getObject(gameObject.getId()))) != -1 ? gameObject : null;
            }
            return null;
        });
    }

    public static TileObject getObject(int[] objectIds) {
        List<GameObject> gameObjects = getGameObjects();
        List<WallObject> wallObjects = getWallObjects();
        List<GroundObject> groundObjects = getGroundObjects();
        List<DecorativeObject> decorationObjects = getDecorationObjects();

        if (gameObjects == null) {
            return null;
        }

        return Microbot.getClientThread().runOnClientThread(() -> {
            for (GameObject obj : gameObjects) {
                if (Arrays.stream(objectIds).anyMatch(id -> obj.getId() == id))
                    if (Calculations.canReach(new ObjectStrategy(obj, getManager().getObject(obj.getId()))))
                        return obj;
            }
            for (WallObject wall : wallObjects) {
                if (Arrays.stream(objectIds).anyMatch(id -> wall.getId() == id))
                    if (Calculations.canReach(new ObjectStrategy(wall, getManager().getObject(wall.getId()))))
                        return wall;
            }
            for (GroundObject ground : groundObjects) {
                if (Arrays.stream(objectIds).anyMatch(id -> ground.getId() == id))
                    if (Calculations.canReach( new ObjectStrategy(ground, getManager().getObject(ground.getId()))))
                        return ground;
            }
            for (DecorativeObject deco : decorationObjects) {
                if (Arrays.stream(objectIds).anyMatch(id -> deco.getId() == id))
                    if (Calculations.canReach(new ObjectStrategy(deco, getManager().getObject(deco.getId()))))
                        return deco;
            }
            return null;
        });
    }

    public static GameObject findObject(String objectName, boolean exact, int distance) {
        List<GameObject> gameObjects = getGameObjects(distance, Microbot.getClient().getLocalPlayer().getWorldLocation());

        if (gameObjects == null) {
            return null;
        }

        for (GameObject gameObject : gameObjects) {
            ObjectComposition objComp = convertGameObjectToObjectComposition(gameObject);

            if (objComp == null) {
                continue;
            }
            String compName = null;

            try {
                compName = !objComp.getName().equals("null") ? objComp.getName() : (objComp.getImpostor() != null ? objComp.getImpostor().getName() : null);
            } catch (Exception e) {
                continue;
            }

            if (compName != null && Microbot.getWalker().canInteract(gameObject.getWorldLocation())) {
                if (!exact && compName.toLowerCase().contains(objectName.toLowerCase())) {
                    return gameObject;
                } else if (exact && compName.equalsIgnoreCase(objectName)) {
                    return gameObject;
                }
            }
        }

        return null;
    }

    public static GameObject findObject(String objectName, boolean exact, int distance, WorldPoint anchorPoint) {
        List<GameObject> gameObjects = getGameObjects(distance, anchorPoint);

        if (gameObjects == null) {
            return null;
        }

        for (GameObject gameObject : gameObjects) {
            ObjectComposition objComp = convertGameObjectToObjectComposition(gameObject);

            if (objComp == null) {
                continue;
            }
            String compName = null;

            try {
                compName = !objComp.getName().equals("null") ? objComp.getName() : (objComp.getImpostor() != null ? objComp.getImpostor().getName() : null);
            } catch (Exception e) {
                continue;
            }

            if (compName != null && Microbot.getWalker().canInteract(gameObject.getWorldLocation())) {
                if (!exact && compName.toLowerCase().contains(objectName.toLowerCase())) {
                    return gameObject;
                } else if (exact && compName.equalsIgnoreCase(objectName)) {
                    return gameObject;
                }
            }
        }

        return null;
    }

    public static boolean hasAction(ObjectComposition objComp, String action) {
        boolean result = false;

        result = Arrays.stream(objComp.getActions()).anyMatch(x -> x != null && x.equals(action));
        if (!result) {
            try {
                result = Arrays.stream(objComp.getImpostor().getActions()).anyMatch(x -> x != null && x.equalsIgnoreCase(action));
            } catch (Exception ex) {
                //do nothing
            }
        }
        return result;
    }

    public static boolean hasAction(GameObject gameObject, String action) {
        ObjectComposition objComp = convertGameObjectToObjectComposition(gameObject);

        boolean result = Arrays.stream(objComp.getActions()).anyMatch(x -> x != null && x.equals(action));
        if (!result) {
            try {
                result = Arrays.stream(objComp.getImpostor().getActions()).anyMatch(x -> x != null && x.equalsIgnoreCase(action));
            } catch (Exception ex) {
                //do nothing
            }
        }
        return result;
    }

    /**
     * Imposter objects are objects that have their menu action changed but still remain the same object.
     * for example: farming patches
     */
    public static GameObject findObjectByImposter(int id, String action) {
        return findObjectByImposter(id, action, true);
    }

    public static GameObject findObjectByImposter(int id, String optionName, boolean exact) {
        List<GameObject> gameObjects = getGameObjects();

        if (gameObjects == null) return null;

        for (net.runelite.api.GameObject gameObject : gameObjects) {

            if (gameObject.getId() != id) continue;

            ObjectComposition objComp = convertGameObjectToObjectComposition(gameObject);

            if (objComp == null) continue;

            try {
                if (objComp.getImpostor() == null) continue;
                if (exact) {
                    if (Arrays.stream(objComp.getImpostor().getActions()).filter(Objects::nonNull)
                            .anyMatch((action) -> action.equalsIgnoreCase(optionName))) {
                        return gameObject;
                    }
                } else {
                    if (Arrays.stream(objComp.getImpostor().getActions()).filter(Objects::nonNull)
                            .anyMatch((action) -> action.toLowerCase().contains(optionName.toLowerCase()))) {
                        return gameObject;
                    }
                }
            } catch (Exception ex) {
                // do nothing
            }
        }

        return null;
    }

    public static GameObject findObjectByOption(String action) {
        return findObjectByOption(action, true);
    }

    public static GameObject findObjectByOption(String optionName, boolean exact) {
        List<GameObject> gameObjects = getGameObjects();

        if (gameObjects.isEmpty()) return null;

        for (net.runelite.api.GameObject gameObject : gameObjects) {

            ObjectComposition objComp = convertGameObjectToObjectComposition(gameObject);

            if (objComp == null) continue;

            if (exact) {
                if (Arrays.stream(objComp.getActions()).filter(Objects::nonNull).anyMatch((action) -> action.equalsIgnoreCase(optionName))) {
                    return gameObject;
                }
            } else {
                if (Arrays.stream(objComp.getActions()).filter(Objects::nonNull).anyMatch((action) -> action.toLowerCase().contains(optionName.toLowerCase()))) {
                    return gameObject;
                }
            }
        }

        return null;
    }


    public static GameObject findBank() {
        List<GameObject> gameObjects = getGameObjects();

        ArrayList<Integer> possibleBankIds = Rs2Reflection.getObjectByName(new String[]{"bank_booth"}, false);

        for (GameObject gameObject : gameObjects) {
            if (possibleBankIds.stream().noneMatch(x -> x == gameObject.getId())) continue;

            ObjectComposition objectComposition = convertGameObjectToObjectComposition(gameObject);

            if (objectComposition == null) continue;

            if (Arrays.stream(objectComposition.getActions())
                    .noneMatch(action ->
                            action != null && (
                                    action.toLowerCase().contains("bank") ||
                                            action.toLowerCase().contains("collect"))))
                continue;

            return gameObject;
        }

        return null;
    }

    public static GameObject findChest() {
        List<GameObject> gameObjects = getGameObjects();

        ArrayList<Integer> possibleBankIds = Rs2Reflection.getObjectByName(new String[]{"chest"}, false);

        for (GameObject gameObject : gameObjects) {
            if (possibleBankIds.stream().noneMatch(x -> x == gameObject.getId())) continue;

            ObjectComposition objectComposition = convertGameObjectToObjectComposition(gameObject);

            if (objectComposition == null) continue;

            if (Arrays.stream(objectComposition.getActions())
                    .noneMatch(action ->
                            action != null && (
                                    action.toLowerCase().contains("bank") ||
                                            action.toLowerCase().contains("collect"))))
                continue;

            return gameObject;
        }

        return null;
    }

    public static GameObject findBank(String action) {
        return findObjectByOption(action, false);
    }

    public static TileObject findObject(int[] ids) {
        TileObject tileObject = null;
        for (int id :
                ids) {
            tileObject = findObjectById(id);
        }
        return tileObject;
    }

    public static ObjectComposition convertGameObjectToObjectComposition(TileObject tileObject) {
        Player player = Microbot.getClient().getLocalPlayer();
        if (player.getLocalLocation().distanceTo(tileObject.getLocalLocation()) > 2400) return null;
        return Microbot.getClientThread().runOnClientThread(() -> Microbot.getClient().getObjectDefinition(tileObject.getId()));
    }

    public static ObjectComposition convertGameObjectToObjectCompositionOnClientThread(TileObject tileObject) {
        Player player = Microbot.getClient().getLocalPlayer();
        if (player.getLocalLocation().distanceTo(tileObject.getLocalLocation()) > 2400) return null;
        return Microbot.getClient().getObjectDefinition(tileObject.getId());
    }

    public static WallObject findDoor(int id) {
        Scene scene = Microbot.getClient().getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = Microbot.getClient().getPlane();
        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    continue;
                }
                WallObject wall = tile.getWallObject();
                if (wall != null && wall.getId() == id)
                    return wall;
            }
        }
        return null;
    }

    public static List<Tile> getTiles(int maxTileDistance) {
        int maxDistance = Math.max(2400, maxTileDistance * 128);

        Player player = Microbot.getClient().getLocalPlayer();
        Scene scene = Microbot.getClient().getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = Microbot.getClient().getPlane();
        List<Tile> tileObjects = new ArrayList<>();
        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    continue;
                }

                if (player.getLocalLocation().distanceTo(tile.getLocalLocation()) <= maxDistance) {
                    tileObjects.add(tile);
                }

            }
        }

        return tileObjects;
    }

    public static List<Tile> getTiles() {
        return getTiles(2400);
    }


    public static GameObject getGameObject(LocalPoint localPoint) {
        Scene scene = Microbot.getClient().getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = Microbot.getClient().getPlane();
        Tile tile = tiles[z][localPoint.getSceneX()][localPoint.getSceneY()];

        return Arrays.stream(tile.getGameObjects()).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public static List<GameObject> getGameObjects() {
        Scene scene = Microbot.getClient().getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = Microbot.getClient().getPlane();
        List<GameObject> tileObjects = new ArrayList<>();
        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    continue;
                }
                for (GameObject tileObject : tile.getGameObjects()) {
                    if (tileObject != null
                            && tileObject.getSceneMinLocation().equals(tile.getSceneLocation()))
                        tileObjects.add(tileObject);
                }
            }
        }
        WorldPoint playerLoc = Microbot.getClient().getLocalPlayer().getWorldLocation();
        return tileObjects.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(tile -> tile.getWorldLocation().distanceTo(playerLoc)))
                .collect(Collectors.toList());
    }

    public static List<GameObject> getGameObjects(int distance, WorldPoint anchorPoint) {
        Scene scene = Microbot.getClient().getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = Microbot.getClient().getPlane();
        List<GameObject> tileObjects = new ArrayList<>();
        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    continue;
                }
                for (GameObject tileObject : tile.getGameObjects()) {
                    if (tileObject != null
                            && tileObject.getSceneMinLocation().equals(tile.getSceneLocation())
                            && tileObject.getWorldLocation().distanceTo2D(anchorPoint) <= distance)
                        tileObjects.add(tileObject);
                }
            }
        }

        return tileObjects.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(tile -> tile.getWorldLocation().distanceTo(Microbot.getClient().getLocalPlayer().getWorldLocation())))
                .collect(Collectors.toList());
    }

    public static List<GroundObject> getGroundObjects() {
        Scene scene = Microbot.getClient().getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = Microbot.getClient().getPlane();
        List<GroundObject> tileObjects = new ArrayList<>();
        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    continue;
                }

                tileObjects.add(tile.getGroundObject());
            }
        }

        return tileObjects.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(tile -> tile.getWorldLocation().distanceTo(Microbot.getClient().getLocalPlayer().getWorldLocation())))
                .collect(Collectors.toList());
    }

    public static List<WallObject> getWallObjects() {
        Scene scene = Microbot.getClient().getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = Microbot.getClient().getPlane();
        List<WallObject> tileObjects = new ArrayList<>();
        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    continue;
                }

                tileObjects.add(tile.getWallObject());
            }
        }


        return tileObjects.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(tile -> tile.getWorldLocation().distanceTo(Microbot.getClient().getLocalPlayer().getWorldLocation())))
                .collect(Collectors.toList());
    }

    // private methods
    private static boolean clickObject(TileObject object) {
        return clickObject(object, "");
    }

    private static boolean clickObject(TileObject object, String action) {
        if (object == null) return false;
        if (Microbot.getClient().getLocalPlayer().getWorldLocation().distanceTo2D(object.getWorldLocation()) > 17) {
            Microbot.getWalker().walkFastCanvas(object.getWorldLocation());
            return false;
        }
        try {
            objectToInteract = object;
            objectAction = action;
            if (Calculations.tileOnScreen(object) && object.getCanvasTilePoly() != null) {
                Microbot.getMouse().click(object.getCanvasTilePoly().getBounds());
            } else {
                Microbot.getMouse().clickFast(Random.random(0, Microbot.getClient().getCanvasWidth()), Random.random(0, Microbot.getClient().getCanvasHeight()));
            }
            sleep(300);
            objectToInteract = null;
            objectAction = null;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return true;
    }

    public static boolean hasLineOfSight(TileObject tileObject) {
        if (tileObject == null) return true;
        if (tileObject instanceof GameObject) {
            GameObject gameObject = (GameObject) tileObject;
            WorldPoint worldPoint = WorldPoint.fromScene(Microbot.getClient(), gameObject.getSceneMinLocation().getX(), gameObject.getSceneMinLocation().getY(), gameObject.getPlane());
            return new WorldArea(
                    worldPoint,
                    gameObject.sizeX(),
                    gameObject.sizeY())
                    .hasLineOfSightTo(Microbot.getClient(), Microbot.getClient().getLocalPlayer().getWorldLocation().toWorldArea());
        }
        return true;
    }

    public static void handleMenuSwapper(MenuEntry menuEntry) {
        if (objectToInteract == null) return;

        try {
            menuEntry.setIdentifier(objectToInteract.getId());
            menuEntry.setType(MenuAction.WALK);//set default to walk to avoid crashing

            ObjectComposition objComp = convertGameObjectToObjectComposition(objectToInteract);
            if (objComp == null) return;

            if (objectToInteract instanceof GameObject) {
                GameObject obj = (GameObject) objectToInteract;
                if (obj.sizeX() > 1) {
                    menuEntry.setParam0(obj.getLocalLocation().getSceneX() - obj.sizeX() / 2);
                } else {
                    menuEntry.setParam0(obj.getLocalLocation().getSceneX());
                }

                if (obj.sizeY() > 1) {
                    menuEntry.setParam1(obj.getLocalLocation().getSceneY() - obj.sizeY() / 2);
                } else {
                    menuEntry.setParam1(obj.getLocalLocation().getSceneY());
                }
            } else {
                // Default objects like walls, groundobjects, decorationobjects etc...
                menuEntry.setParam0(objectToInteract.getLocalLocation().getSceneX());
                menuEntry.setParam1(objectToInteract.getLocalLocation().getSceneY());
            }

            menuEntry.setTarget("");
            menuEntry.setOption(objectAction == null ? "" : objectAction);

            int index = -1;
            if (objectAction != null && !objectAction.isEmpty()) {
                String[] actions;
                if (objComp.getImpostorIds() != null) {
                    actions = objComp.getImpostor().getActions();
                } else {
                    actions = objComp.getActions();
                }

                for (int i = 0; i < actions.length; i++) {
                    if (objectAction.equalsIgnoreCase(actions[i])) {
                        index = i;
                        break;
                    }
                }
            } else {
                index = 0;
            }

            if (Microbot.getClient().isWidgetSelected()) {
                Rs2Reflection.setItemId(menuEntry, -1);
                menuEntry.setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT);
            } else if (index == 0) {
                menuEntry.setType(MenuAction.GAME_OBJECT_FIRST_OPTION);
            } else if (index == 1) {
                menuEntry.setType(MenuAction.GAME_OBJECT_SECOND_OPTION);
            } else if (index == 2) {
                menuEntry.setType(MenuAction.GAME_OBJECT_THIRD_OPTION);
            } else if (index == 3) {
                menuEntry.setType(MenuAction.GAME_OBJECT_FOURTH_OPTION);
            } else if (index == 4) {
                menuEntry.setType(MenuAction.GAME_OBJECT_FIFTH_OPTION);
            }
        } catch (Exception ex) {
            System.out.println("GAME OBJECT MENU SWAP FAILED WITH MESSAGE: " + ex.getMessage());
        }
    }
}
