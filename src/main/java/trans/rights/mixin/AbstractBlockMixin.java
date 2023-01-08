package trans.rights.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import trans.rights.impl.hack.WallHack;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @Inject(method = "getAmbientOcclusionLightLevel", at = @At("HEAD"), cancellable = true)
    private void getAmbientOcclusionLevel(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir){
        if (WallHack.INSTANCE.getOpacity().getValue() == WallHack.Opacity.SOME && WallHack.INSTANCE.isEnabled()) cir.setReturnValue(1f);
    }
}
