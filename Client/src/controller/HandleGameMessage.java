/*     private void handleMessage(Message message) { //TODO: This has to handle both local and online play
        switch (message.getMessageType()) {
            case SEND_EXCEPTION:
                showError(message);
                break;
            case ACCOUNT_COPY:
                updateAccount(message);
                break;
            case GAME_COPY: // this starts the game
                GameController.getInstance().setCurrentGame(message.getGameCopyMessage().getCompressedGame());
                GameController.getInstance().calculateAvailableActions();
                break;
            case ORIGINAL_CARDS_COPY:
                ShopController.getInstance().setOriginalCards(message.getCardsCopyMessage().getCards());
                break;
            case LEADERBOARD_COPY:
                MainMenuController.getInstance().setLeaderBoard(message.getLeaderBoardCopyMessage().getLeaderBoard());
                break;
            case STORIES_COPY:
                StoryMenuController.getInstance().setStories(message.getStoriesCopyMessage().getStories());
                break;
            case CARD_POSITION://TODO:CHANGE
                CardPosition cardPosition = message.getCardPositionMessage().getCardPosition();
                switch (cardPosition) {
                    case MAP:
                        GameController.getInstance().getCurrentGame().moveCardToMap(message.getCardPositionMessage().getCompressedCard());
                        GameController.getInstance().calculateAvailableActions();
                        break;
                    case HAND:
                        GameController.getInstance().getCurrentGame().moveCardToHand();
                        GameController.getInstance().calculateAvailableActions();
                        break;
                    case NEXT:
                        GameController.getInstance().getCurrentGame().moveCardToNext(message.getCardPositionMessage().getCompressedCard());
                        GameController.getInstance().calculateAvailableActions();
                        break;
                    case GRAVE_YARD:
                        GameController.getInstance().getCurrentGame().moveCardToGraveYard(message.getCardPositionMessage().getCompressedCard());
                        GameController.getInstance().calculateAvailableActions();
                        break;
                    case COLLECTED:
                        GameController.getInstance().getCurrentGame().moveCardToCollectedItems(message.getCardPositionMessage().getCompressedCard());
                        GameController.getInstance().calculateAvailableActions();
                        break;
					// TODO: consider move GameController.getInstance() here
                }
                break;
            case TROOP_UPDATE:
                GameController.getInstance().getCurrentGame().troopUpdate(message.getTroopUpdateMessage().getCompressedTroop());
                GameController.getInstance().calculateAvailableActions();
                break;
            case GAME_UPDATE:
                GameUpdateMessage gameUpdateMessage = message.getGameUpdateMessage();
                GameController.getInstance().getCurrentGame().gameUpdate(
                        gameUpdateMessage.getTurnNumber(),
                        gameUpdateMessage.getPlayer1CurrentMP(),
                        gameUpdateMessage.getPlayer1NumberOfCollectedFlags(),
                        gameUpdateMessage.getPlayer2CurrentMP(),
                        gameUpdateMessage.getPlayer2NumberOfCollectedFlags(),
                        gameUpdateMessage.getCellEffects());
                GameController.getInstance().calculateAvailableActions();
                break;
            case Game_FINISH:
                GameResultController.getInstance().setWinnerInfo(message.getGameFinishMessage().amIWinner(), message.getGameFinishMessage().getReward());
                if (currentShow instanceof BattleScene) {
                    ((BattleScene) currentShow).finish(message.getGameFinishMessage().amIWinner());
                }
                new Thread(() -> { // wtf ?
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                    if (((BattleScene) currentShow).getMyPlayerNumber() == -1) {
                        Platform.runLater(() -> new MainMenu().show());
                    } else
                        Platform.runLater(() -> new GameResultMenu().show());
                }).start();
                break;
            case ANIMATION:
                GameController.getInstance().showAnimation(message.getGameAnimations());
                break;
            case DONE:
                break;
            case CHAT:
                showOrSaveMessage(message);
                break;
            case INVITATION:
                Platform.runLater(() -> currentShow.showInvite(message.getNewGameFields()));
                break;
            case ACCEPT_REQUEST:
                //think...
                break;
            case DECLINE_REQUEST:
                if (currentShow instanceof WaitingMenu) {
                    ((WaitingMenu) currentShow).close();
                }
                break;
            case CHANGE_CARD_NUMBER:
                ShopAdminController.getInstance().setValue(
                        message.getChangeCardNumber().getCardName(),
                        message.getChangeCardNumber().getNumber()
                );
                break;
            case ADD_TO_ORIGINALS:
                if (ShopController.isLoaded()) {
                    ShopController.getInstance().addCard(message.getCard());
                }
                break;
            case ADD_TO_CUSTOM_CARDS:
                CustomCardRequestsController.getInstance().addCard(message.getCard());
                break;
            case REMOVE_FROM_CUSTOM_CARDS:
                CustomCardRequestsController.getInstance().removeCard(message.getCardName());
                break;
            case CUSTOM_CARDS_COPY:
                CustomCardRequestsController.getInstance().setCustomCardRequests(message.getCardsCopyMessage().getCards());
                break;
            case ONLINE_GAMES_COPY:
                OnlineGamesListController.getInstance().setOnlineGames(message.getOnlineGames());
                break;
        }
    } */