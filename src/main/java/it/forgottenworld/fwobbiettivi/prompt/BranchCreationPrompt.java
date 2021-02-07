package it.forgottenworld.fwobbiettivi.prompt;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.managers.Branches;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BranchCreationPrompt implements ConversationAbandonedListener {

    private ConversationFactory conversationFactory;

    public BranchCreationPrompt(FWObbiettivi plugin) {
        this.conversationFactory = new ConversationFactory(plugin)
                .withModality(false)
                .withFirstPrompt(new BranchCreationPrompt.BranchIdPrompt())
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

    private class BranchIdPrompt extends FixedSetPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_BRANCH_CREATE_INIT);
            promptMessage = promptMessage.concat( "\n" + ChatFormatter.formatPromptMessageNoPrefix(Messages.PROMPT_BRANCH_CREATE_ID));
            return promptMessage;
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            return !Branches.getRami().contains(input) && !input.contains(" ");
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, String input) {
            context.setSessionData("branchId", input);
            return new BranchMaterialPrompt();
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return ChatFormatter.formatErrorMessage(Messages.PROMPT_BRANCH_CREATE_ID_FAIL);
        }
    }

    private class BranchMaterialPrompt extends FixedSetPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_BRANCH_CREATE_MATERIAL);
            return promptMessage;
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            return (Material.getMaterial(input) != null) && !input.contains(" ");
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, String input) {
            context.setSessionData("branchMaterial", input);
            return new BranchDescriptionPrompt();
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return ChatFormatter.formatErrorMessage(Messages.PROMPT_BRANCH_CREATE_MATERIAL_FAIL);
        }

    }

    private class BranchDescriptionPrompt extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatFormatter.formatPromptMessage(Messages.PROMPT_BRANCH_CREATE_DESCRIPTION);
            return promptMessage;
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            context.setSessionData("branchDescription", input);
            return new CreateBranchPrompt();
        }

    }

    private class CreateBranchPrompt extends MessagePrompt {

        public String getPromptText(ConversationContext context) {
            String branchId = (String) context.getSessionData("branchId");
            String branchMaterial = (String) context.getSessionData("branchMaterial");
            String branchDescription = (String) context.getSessionData("branchDescription");

            ItemStack icon = new ItemStack(Material.getMaterial(branchMaterial));
            List<String> description = Arrays.asList(branchDescription.split("(?<=\\G.{50})"));

            Branch b = new Branch(branchId, icon, description);
            Branches.addBranch(b);

            Player player = (Player) context.getForWhom();
            player.playSound( player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1 );

            return ChatFormatter.formatSuccessMessage(Messages.BRANCH_ADDED + " " + ChatFormatter.formatWarningMessageNoPrefix(branchId));
        }

        @Override
        protected Prompt getNextPrompt(ConversationContext context) {
            return Prompt.END_OF_CONVERSATION;
        }

    }
}
