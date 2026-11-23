package local.hexcanvas.mixin;

import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GuiSpellcasting.class, remap = false)
public abstract class GuiSpellcastingMixin {
    @Unique
    private float hexcanvas$offsetX;

    @Unique
    private float hexcanvas$offsetY;

    @Unique
    private boolean hexcanvas$panning;

    @Unique
    private double hexcanvas$lastMouseX;

    @Unique
    private double hexcanvas$lastMouseY;

    @Inject(method = "coordsOffset", at = @At("RETURN"), cancellable = true, remap = false)
    private void hexcanvas$applyCameraOffset(CallbackInfoReturnable<Vec2> cir) {
        Vec2 original = cir.getReturnValue();
        cir.setReturnValue(original.add(new Vec2(hexcanvas$offsetX, hexcanvas$offsetY)));
    }

    @Inject(method = "m_6375_", at = @At("HEAD"), cancellable = true, remap = false)
    private void hexcanvas$startPan(
        double mouseX,
        double mouseY,
        int button,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (button == 1) {
            hexcanvas$panning = true;
            hexcanvas$lastMouseX = mouseX;
            hexcanvas$lastMouseY = mouseY;
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "m_7979_", at = @At("HEAD"), cancellable = true, remap = false)
    private void hexcanvas$pan(
        double mouseX,
        double mouseY,
        int button,
        double dragX,
        double dragY,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (button == 1 && hexcanvas$panning) {
            hexcanvas$offsetX += (float) (mouseX - hexcanvas$lastMouseX);
            hexcanvas$offsetY += (float) (mouseY - hexcanvas$lastMouseY);
            hexcanvas$lastMouseX = mouseX;
            hexcanvas$lastMouseY = mouseY;
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "m_6348_", at = @At("HEAD"), cancellable = true, remap = false)
    private void hexcanvas$stopPan(
        double mouseX,
        double mouseY,
        int button,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (button == 1) {
            hexcanvas$panning = false;
            cir.setReturnValue(true);
        }
    }
}
