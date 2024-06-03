package org.vaadin.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import rea.Manager;
import rea.ReaException;
import rea.SceneLayout;
import rea.components.Character;
import rea.gameplay.games.CartoonAvatar;
import rea.gaming.GameInstance;
import rea.gaming.Player;
import rea.ui.GamePanel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static rea.gameplay.games.CartoonAvatar.*;

@Route
public class MainView extends VerticalLayout {

    private final Grid<GameInstance> gameGrid = new Grid<>(GameInstance.class, false);
    private final Grid<GameInstance> currentGameGrid = new Grid<>(GameInstance.class, false);
    private Map<Div, GameInstance> gameInstancePanelMap = new HashMap<>();
    private final Manager manager;
    private Tab currentGamesTab;
    private Tab manageGamesTab;
    private Tabs menu;
    private Div content;
    private VerticalLayout gamesContent;
    private HorizontalLayout currentGamesContent;
    private Button selectedAvatarButton;
    private Button selectedGameTypeButton;
    private CartoonAvatar playerAvatar = null;
    private String gameType = "";
    private Player currentPlayer;
    private final HorizontalLayout gameLayout = new HorizontalLayout();
    private Div gamePanelLayout;

    public MainView() throws ReaException {

        this.manager = Manager.getInstance();

        manageGamesTab = new Tab(VaadinIcon.BULLETS.create(), new Span("View / Manage Games"));
        currentGamesTab = new Tab(VaadinIcon.DOT_CIRCLE.create(), new Span("Current Games"));

        menu = new Tabs(manageGamesTab, currentGamesTab);
        menu.setOrientation(Tabs.Orientation.VERTICAL);
        menu.setWidthFull();

        content = new Div();
        content.setWidthFull();

        configureGameGrid();
        configureCurrentGameGrid();
        updateGameGrid(manager.getGameInstances());
        updateCurrentGameGrid(manager.getGameInstances());

        Button newGameButton = new Button("Start New Game", event -> openNewGameDialog(manager));
        newGameButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);

        gamesContent = new VerticalLayout(newGameButton, gameGrid);
        gamesContent.setWidthFull();

        currentGamesContent = new HorizontalLayout(currentGameGrid);
        currentGamesContent.setMaxWidth("1500px");

        add(menu, content);

        content.add(gamesContent);

        menu.addSelectedChangeListener(event -> {
            content.removeAll();
            Tab selectedTab = event.getSelectedTab();
            if (selectedTab.equals(manageGamesTab)) {
                content.add(gamesContent);
            } else if (selectedTab.equals(currentGamesTab)) {
                content.add(currentGamesContent);
                content.add(gameLayout);
            }
        });

        manager.addGamesUpdateListener(event -> {
            updateGameGrid(event.getGamesToPlay());
            updateCurrentGameGrid(event.getGamesToPlay());
        });
    }

    private String getPlayerNamesAvatars(GameInstance gameInstance) {
        return gameInstance.getPlayers().stream()
                .map(player -> {
                    String avatarVisual = playerAvatar.getAvatarVisual().getPathname();
                    String name = player.getCharacter().getName();
                    return "<div style='display: inline-block; text-align: center; margin: 5px;'>" +
                            "<img src='" + avatarVisual +
                            "' alt='Avatar' width='40' height='40' style='display: block;'/>" +
                            "<span>" + name + "</span>" +
                            "</div>";
                })
                .collect(Collectors.joining());
    }

    private void openNewGamePanel(GameInstance gameInstance) {
        clearCurrentGamePanel(gameInstance);
        GamePanel currentPanel = new GamePanel(gameInstance.getGameMap());
        currentPanel.setWidth("1200px");
        currentPanel.setHeightFull();
        //SceneLayout playerOnScene = new SceneLayout();

        //currentPanel.add(playerOnScene);

        HorizontalLayout gameInfoLayout = new HorizontalLayout();
        Span gameName = new Span("Game: " + gameInstance.getName());
        Span gameDescription = new Span("Objective: " + gameInstance.getDescription());
        Span playerInventory = new Span("Your inventory: " + currentPlayer.getCharacter().getInventory());
        gameInfoLayout.add(gameName, gameDescription, playerInventory);
        gameInfoLayout.setSpacing(true);

        String playerNamesAvatarsHtml = getPlayerNamesAvatars(gameInstance);
        Html playerNamesAvatars = new Html("<div>Players: " + playerNamesAvatarsHtml + "</div>");

        VerticalLayout playerInfoLayout = new VerticalLayout();
        playerInfoLayout.add(playerNamesAvatars);

        gamePanelLayout = new Div();
        gamePanelLayout.add(gameInfoLayout, playerInfoLayout, currentPanel);

        gameLayout.add(gamePanelLayout);

        gameInstancePanelMap.put(gamePanelLayout, gameInstance);

        gameInfoLayout.getStyle().set("background-color", "#f0f0f0");
        gameInfoLayout.getStyle().set("border", "1px solid #ccc");
        gameInfoLayout.getStyle().set("border-radius", "5px");
        gameInfoLayout.getStyle().set("padding", "10px");
        gameInfoLayout.getStyle().set("font-family", "Arial, sans-serif");
        gameInfoLayout.getStyle().set("color", "#333");

        playerInfoLayout.getStyle().set("box-shadow", "0px 2px 5px rgba(0, 0, 0, 0.1)");
        playerInfoLayout.getStyle().set("border", "1px solid #ddd");
        playerInfoLayout.getStyle().set("border-radius", "5px");
        playerInfoLayout.getStyle().set("padding", "10px");

    }

    private Div getGamePanelDiv(GameInstance gameInstance) {
        for (Div panel : gameInstancePanelMap.keySet()) {
            if (gameInstancePanelMap.get(panel).equals(gameInstance)) {
                return panel;
            }
        }
        return null;
    }

    private void clearCurrentGamePanel(GameInstance game) {
        Div gamePanel = getGamePanelDiv(game);
        if (gamePanel != null) {
            gameLayout.remove(gamePanel);
            gameInstancePanelMap.remove(gamePanel);
        }
    }

    private void configureGameGrid() {
        gameGrid.addColumn(GameInstance::getName).setHeader("Game");
        gameGrid.addColumn(GameInstance::getPlayerCount).setHeader("Players");
        gameGrid.addColumn(GameInstance::getPlayingSince).setHeader("Playing Since");
        gameGrid.addColumn(game -> game.isNotPlayingYet() ? "Not Started" : "In Progress").setHeader("Status");
        gameGrid.addColumn(GameInstance::getCurrentStage).setHeader("Stage");
        gameGrid.addComponentColumn(game -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button joinButton = new Button("Join");
            joinButton.addClickListener(event -> openJoinGameDialog(manager, game));
            joinButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            Button deleteButton = new Button("Delete");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            deleteButton.addClickListener(event -> deleteGame(manager, game));
            actions.add(joinButton, deleteButton);
            return actions;
        }).setHeader("Actions");
        gameGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
    }

    private void configureCurrentGameGrid() {
        currentGameGrid.addColumn(GameInstance::getName).setHeader("Game");
        currentGameGrid.addColumn(GameInstance::getPlayerCount).setHeader("Players");
        currentGameGrid.addColumn(GameInstance::getPlayingSince).setHeader("Playing Since");
        currentGameGrid.addColumn(GameInstance::getCurrentStage).setHeader("Stage");
        currentGameGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

//        currentGameGrid.addItemClickListener(event -> {
//            GameInstance gameInstance = event.getItem();
//            GamePanel gamePanel = getGamePanel(gameInstance);
//            gamePanelLayout.add(gamePanel);
//        });

        GridContextMenu<GameInstance> currentGameActions = new GridContextMenu<>(currentGameGrid);

        currentGameActions.addItem("Leave Game", event ->
            event.getItem().ifPresent(game -> {
            leaveCurrentGame(game, currentPlayer);
            menu.setSelectedTab(manageGamesTab);
            })
        );
        currentGameActions.addItem("End Game", event -> {
            event.getItem().ifPresent(this::endCurrentGame);
            menu.setSelectedTab(manageGamesTab);
        });

    }

    private void updateGameGrid(List<GameInstance> gameInstances) {
        gameGrid.setItems(gameInstances);
    }

    private void updateCurrentGameGrid(List<GameInstance> gameInstances) {
        currentGameGrid.setItems(gameInstances.stream().filter(game -> !game.isNotPlayingYet()).toList());
    }

    private Button createAvatarButton(String imagePath, String avatarName) {
        Avatar avatarImage = new Avatar(avatarName);
        avatarImage.setImage(imagePath);
        Button avatarButton = new Button(avatarImage);

        avatarButton.addClickListener(event -> {
            if (selectedAvatarButton != null) {
                selectedAvatarButton.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            }
            selectedAvatarButton = avatarButton;
            selectedAvatarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            switch (avatarName) {
                case "Bunny":
                    playerAvatar = BUNNY;
                    break;
                case "Chick":
                    playerAvatar = CHICK;
                    break;
                case "Lamb":
                    playerAvatar = LAMB;
                    break;
                default:
                    break;
            }
        });

        return avatarButton;
    }

    private Button createGameTypeButton(String gameType) {
        Button gameTypeButton = new Button(gameType);

        gameTypeButton.addClickListener(event -> {
            if (selectedGameTypeButton != null) {
                selectedGameTypeButton.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            }
            selectedGameTypeButton = gameTypeButton;
            selectedGameTypeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            this.gameType = gameType;
        });

        return gameTypeButton;
    }

    private Button createRejectButton(Dialog dialog, String text) {
        Button rejectButton = new Button(text, event -> {
            dialog.close();
            reset();
        });
        rejectButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return rejectButton;
    }

    private void openNewGameDialog(Manager manager) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        Span chooseGameType = new Span("Type of Game: ");

        Button treasureHuntButton = createGameTypeButton("Treasure Hunt");
        Button easterEggRaceButton = createGameTypeButton("Easter Egg Race");

        HorizontalLayout gameTypeLayout = new HorizontalLayout(treasureHuntButton, easterEggRaceButton);

        Button saveButton = new Button("Save", event -> {
            if (!gameType.isEmpty()) {
                manager.createGameInstance(gameType);
                updateGameGrid(manager.getGameInstances());
                dialog.close();
                reset();
            } else {
                Notification.show("Please choose your game");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = createRejectButton(dialog, "Cancel");

        HorizontalLayout buttons = new HorizontalLayout(cancelButton, saveButton);
        buttons.setWidthFull();
        buttons.setJustifyContentMode(JustifyContentMode.END);

        FormLayout formLayout = new FormLayout(chooseGameType, gameTypeLayout);
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttons);

        dialog.add(dialogLayout);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.open();
    }

    private void openJoinGameDialog(Manager manager, GameInstance game) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        TextField nameField = new TextField("Your Name");
        Span chooseAvatarLabel = new Span("Choose your avatar:");

        Button bunnyButton = createAvatarButton("images/bunny.png", "Bunny");
        Button chickButton = createAvatarButton("images/chick.png", "Chick");
        Button lambButton = createAvatarButton("images/lamb.png", "Lamb");

        HorizontalLayout avatarLayout = new HorizontalLayout(bunnyButton, chickButton, lambButton);

        Button saveButton = new Button("Join", event -> {
            String name = nameField.getValue();
            if (!name.isEmpty() && playerAvatar != null && playerAvatar.getAvatarName() != null
                    && !playerAvatar.getAvatarName().isEmpty()) {

                Character newPlayer = new Character(name, playerAvatar);
                currentPlayer = game.addPlayer(newPlayer);

                if (game.canStart()) {
                    game.startPlayingGame();
                }

                if (!game.canJoin()) {
                    Notification.show ("Cannot join game: Game full");
                }

                updateCurrentGameGrid(manager.getGameInstances());

                openNewGamePanel(game);
                menu.setSelectedTab(currentGamesTab);

                dialog.close();
                reset();
            } else {
                Notification.show("Please fill in all fields");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        Button cancelButton = createRejectButton(dialog, "Cancel");

        HorizontalLayout buttons = new HorizontalLayout(cancelButton, saveButton);
        buttons.setWidthFull();
        buttons.setJustifyContentMode(JustifyContentMode.END);

        FormLayout formLayout = new FormLayout(nameField, chooseAvatarLabel, avatarLayout);
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttons);

        dialog.add(dialogLayout);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.open();
    }

    private void deleteGame(Manager manager, GameInstance game) {
        if (game.canDelete()) {
            manager.deleteGameInstance(game);
            updateGameGrid(manager.getGameInstances());
            reset();
            clearCurrentGamePanel(game);
            Notification.show("Game deleted successfully");
        } else {
            Notification.show("Cannot delete this game");
        }
    }

    private void leaveCurrentGame(GameInstance gameInstance, Player player) {
        gameInstance.removePlayer(player);
        clearCurrentGamePanel(gameInstance);
    }

    private void endCurrentGame(GameInstance gameInstance) {
        gameInstance.endPlayingGame();
        clearCurrentGamePanel(gameInstance);
    }

    private void reset() {
        gameType = "";
        playerAvatar = null;
    }

}
