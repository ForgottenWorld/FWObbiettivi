package it.forgottenworld.fwobbiettivi.prompt;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.managers.Branches;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.ConfigUtil;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import org.bukkit.Sound;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoalCreationPrompt implements ConversationAbandonedListener {

    private ConversationFactory conversationFactory;

    public GoalCreationPrompt(FWObbiettivi plugin) {
        this.conversationFactory = new ConversationFactory(plugin)
                .withModality(false)
                .withFirstPrompt(new GoalIdPrompt())
                .withEscapeSequence("cancel")
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage(Messages.NO_CONSOLE)
                .addConversationAbandonedListener(this);
    }

    public void startConversationForPlayer(Player player) {
        conversationFactory.buildConversation(player).begin();
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent abandonedEvent) {
        if (abandonedEvent.gracefulExit()) {
            abandonedEvent.getContext().getForWhom().sendRawMessage(ChatFormatter.formatSuccessMessage("Setup completed!"));
        } else {
            abandonedEvent.getContext().getForWhom().sendRawMessage(ChatFormatter.formatErrorMessage("Setup canceled!"));
        }
    }

    private class GoalIdPrompt extends FixedSetPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_GOAL_CREATE_INIT);
            promptMessage = promptMessage.concat( "\n" + ChatFormatter.formatPromptMessageNoPrefix(Messages.PROMPT_GOAL_CREATE_ID));
            return promptMessage;
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            return !Goals.getObbiettivi().contains(input) && !input.contains(" ");
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, String input) {
            context.setSessionData("goalId", input);
            return new GoalBranchPrompt();
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return ChatFormatter.formatErrorMessage(Messages.PROMPT_GOAL_CREATE_ID_FAIL);
        }
    }

    private class GoalBranchPrompt extends FixedSetPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_GOAL_CREATE_BRANCH);
            return promptMessage;
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            return (Branches.getRami().stream().filter(p -> p.getName().equals(input)).count() > 0 || ConfigUtil.getTreasuryName().equals(input)) && !input.contains(" ");
        }

        @Override
        public Prompt acceptValidatedInput(ConversationContext context, String input) {
            context.setSessionData("goalBranch", input);
            return new GoalMaxPlotPrompt();
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return ChatFormatter.formatErrorMessage(Messages.PROMPT_GOAL_CREATE_BRANCH_FAIL);
        }
    }

    private class GoalMaxPlotPrompt extends NumericPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_GOAL_CREATE_PLOT);
            return promptMessage;
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            context.setSessionData("goalMaxPlot", input);
            return new GoalRequiredGoalsPrompt();
        }

        @Override
        protected boolean isNumberValid(ConversationContext context, Number input) {
            return input.intValue() >= 1;
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
            return ChatFormatter.formatErrorMessage(Messages.PROMPT_GOAL_CREATE_PLOT_FAIL);
        }
    }

    private class GoalRequiredGoalsPrompt extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_GOAL_CREATE_REQGOALS);
            return promptMessage;
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            context.setSessionData("goalRequiredGoals", input);
            return new GoalRequiredZenarPrompt();
        }

    }

    private class GoalRequiredZenarPrompt extends NumericPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_GOAL_CREATE_REQZENAR);
            return promptMessage;
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            context.setSessionData("goalRequiredZenar", input);
            return new GoalRewardZenarPrompt();
        }

        @Override
        protected boolean isNumberValid(ConversationContext context, Number input) {
            return input.doubleValue() >= 0.0;
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
            return ChatFormatter.formatErrorMessage(Messages.PROMPT_GOAL_CREATE_REQZENAR_FAIL);
        }
    }

    private class GoalRewardZenarPrompt extends NumericPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_GOAL_CREATE_REWZENAR);
            return promptMessage;
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            context.setSessionData("goalRewardZenar", input);
            return new GoalRequiredPermissionsPrompt();
        }

        @Override
        protected boolean isNumberValid(ConversationContext context, Number input) {
            return input.doubleValue() >= 0.0;
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
            return ChatFormatter.formatErrorMessage(Messages.PROMPT_GOAL_CREATE_REWZENAR_FAIL);
        }
    }

    private class GoalRequiredPermissionsPrompt extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_GOAL_CREATE_REWPERMS);
            return promptMessage;
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            context.setSessionData("goalRequiredPermissions", input);
            return new GoalRewardPluginNamePrompt();
        }

    }

    private class GoalRewardPluginNamePrompt extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_GOAL_CREATE_REWMULTI);
            return promptMessage;
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            context.setSessionData("goalRewardPluginName", input);
            return new GoalDescriptionPrompt();
        }

    }


    private class GoalDescriptionPrompt extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_GOAL_CREATE_DESCRIPTION);
            return promptMessage;
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            context.setSessionData("goalDescription", input);
            return new CreateGoalPrompt();
        }

    }

    private class CreateGoalPrompt extends MessagePrompt {

        public String getPromptText(ConversationContext context) {
            String goalId = (String) context.getSessionData("goalId");
            String goalBranch = (String) context.getSessionData("goalBranch");
            int goalMaxPlot = (int) context.getSessionData("goalMaxPlot");
            String goalRequiredGoals = (String) context.getSessionData("goalRequiredGoals");
            double goalRequiredZenar = (int) context.getSessionData("goalRequiredZenar");
            double goalRewardZenar = (int) context.getSessionData("goalRewardZenar");
            String goalRequiredPermissions = (String) context.getSessionData("goalRequiredPermissions");
            String goalRewardPluginName = (String) context.getSessionData("goalRewardPluginName");
            String goalDescription = (String) context.getSessionData("goalDescription");

            List<String> requiredGoals = Arrays.asList(goalRequiredGoals.split(", "));
            List<String> requiredPermissions = goalRequiredPermissions.equals("skip") ? new ArrayList<>() : Arrays.asList(goalRequiredPermissions.split(", "));
            String rewardPluginName = goalRewardPluginName.equals("skip") ? "" : goalRewardPluginName;
            List<String> description = Arrays.asList(goalDescription.split("(?<=\\G.{50})"));

            Goal g = new Goal(goalId, Branches.getBranchFromString(goalBranch), goalMaxPlot, requiredGoals, goalRequiredZenar, goalRewardZenar, requiredPermissions, rewardPluginName, description);
            Goals.addGoal(g);

            Player player = (Player) context.getForWhom();
            player.playSound( player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1 );

            return ChatFormatter.formatSuccessMessage(Messages.GOAL_ADDED + " " + ChatFormatter.formatWarningMessageNoPrefix(goalId));
        }

        @Override
        protected Prompt getNextPrompt(ConversationContext context) {
            return Prompt.END_OF_CONVERSATION;
        }

    }

}
