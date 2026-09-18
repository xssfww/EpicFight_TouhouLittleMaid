package net.EFTLM.EF.Command;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.RegisterCommandsEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import java.util.Set;
public class MaidSkillCommand {
    public static void Build(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("maid")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("clear")
                                .executes(context -> ClearMaidSkill(
                                        context.getSource().getPlayerOrException()
                                ))
                        )
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("add")
                                .then(Commands.argument("RegisterName", ResourceLocationArgument.id())
                                        .suggests(SkillSuggestion)
                                        .executes(context -> AddMaidSkill(
                                                context.getSource().getPlayerOrException(),
                                                ResourceLocationArgument.getId(context, "RegisterName")
                                        ))
                                )
                        )
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("remove")
                                .then(Commands.argument("RegisterName", ResourceLocationArgument.id())
                                        .suggests(SkillSuggestion)
                                        .executes(context -> RemoveMaidSkill(
                                                context.getSource().getPlayerOrException(),
                                                ResourceLocationArgument.getId(context, "RegisterName")
                                        ))
                                )
                        )
        );
    }
    protected static final SuggestionProvider<CommandSourceStack> SkillSuggestion = (context, builder) -> {
        Set<ResourceLocation> Skills = MaidSkillManager.getSkillRegisterName();
        Skills.forEach(RL -> builder.suggest(RL.toString()));
        return builder.buildFuture();
    };
    protected static int AddMaidSkill(ServerPlayer player, ResourceLocation name) {
        if (MaidSkillManager.hasSkillFor(name)) {
            MaidSkill Skill = MaidSkillManager.getSkillFor(name);
            if (Skill != null) {
                if (player.level() instanceof ServerLevel level) {
                    int count = 0;
                    for (Entity entity : level.getAllEntities()) {
                        if (entity instanceof EntityMaid maid) {
                            if (maid.getOwnerUUID() != null) {
                                if (player.getUUID().equals(maid.getOwnerUUID())) {
                                    MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
                                    if (MaidPatch != null) {
                                        if (!MaidPatch.hasLearnedSkill(name)) {
                                            MaidPatch.addLearnedSkill(name);
                                            count++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (count > 0) {
                        player.sendSystemMessage(Component.translatable("message.eftlm.add_maid_skill_success", count, Skill.getTitle().getString()));
                    } else {
                        player.sendSystemMessage(Component.translatable("message.eftlm.add_maid_skill_failure"));
                    }
                    return count;
                }
            }
        }
        return 0;
    }
    protected static int RemoveMaidSkill(ServerPlayer player, ResourceLocation name) {
        if (MaidSkillManager.hasSkillFor(name)) {
            MaidSkill Skill = MaidSkillManager.getSkillFor(name);
            if (Skill != null) {
                if (player.level() instanceof ServerLevel level) {
                    int count = 0;
                    for (Entity entity : level.getAllEntities()) {
                        if (entity instanceof EntityMaid maid) {
                            if (maid.getOwnerUUID() != null) {
                                if (player.getUUID().equals(maid.getOwnerUUID())) {
                                    MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
                                    if (MaidPatch != null) {
                                        if (MaidPatch.hasLearnedSkill(name)) {
                                            MaidPatch.removeLearnedSkill(name);
                                            MaidPatch.removeData(Skill);
                                            count++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (count > 0) {
                        player.sendSystemMessage(Component.translatable("message.eftlm.remove_maid_skill_success", count, Skill.getTitle().getString()));
                    } else {
                        player.sendSystemMessage(Component.translatable("message.eftlm.remove_maid_skill_failure"));
                    }
                    return count;
                }
            }
        }
        return 0;
    }
    protected static int ClearMaidSkill(ServerPlayer player) {
        if (player.level() instanceof ServerLevel level) {
            int count = 0;
            for (Entity entity : level.getAllEntities()) {
                if (entity instanceof EntityMaid maid) {
                    if (maid.getOwnerUUID() != null) {
                        if (player.getUUID().equals(maid.getOwnerUUID())) {
                            MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
                            if (MaidPatch != null) {
                                for (ResourceLocation name : MaidPatch.getLearnedSkills()) {
                                    if (MaidSkillManager.hasSkillFor(name)) {
                                        MaidSkill Skill = MaidSkillManager.getSkillFor(name);
                                        if (Skill != null) {
                                            MaidPatch.removeData(Skill);
                                        }
                                    }
                                }
                                MaidPatch.clearLearnedSkills();
                                count++;
                            }
                        }
                    }
                }
            }
            player.sendSystemMessage(Component.translatable("message.eftlm.clear_maid_skill", count));
        }
        return 0;
    }
    public static void RegisterCommands(RegisterCommandsEvent event) {
        MaidSkillCommand.Build(event.getDispatcher());
    }
}
