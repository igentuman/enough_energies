package igentuman.ee.mixin;

import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.*;
import com.denfop.utils.ModUtils;
import net.minecraftforge.fluids.FluidTank;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Component.class)
public abstract class IuComponentMixin<T> {

    @Shadow public abstract T getComponent();

    @Inject(method = "getText", at = @At("HEAD"), cancellable = true, remap=false)
    public void getText(GuiComponent guiComponent, CallbackInfoReturnable<String> cir) {
        String text = "";

        if (this.getComponent() instanceof ComponentButton) {

            ComponentButton component = (ComponentButton) this.getComponent();
            text = component.getText();

        } else if (this.getComponent() instanceof AdvEnergy) {

            AdvEnergy component = (AdvEnergy) this.getComponent();
            text =
                    ModUtils.getString(Math.min(
                            component.getEnergy(),
                            component.getCapacity()
                    )) + "/" + ModUtils.getString(component.getCapacity()) + " " +
                            "RF";

        } else if (this.getComponent() instanceof ComponentBaseEnergy) {

            ComponentBaseEnergy component = (ComponentBaseEnergy) this.getComponent();
            text =
                    ModUtils.getString(Math.min(
                            component.getEnergy(),
                            component.getCapacity()
                    )) + "/" + ModUtils.getString(component.getCapacity()) + component.getType().getPrefix();

        } else if (this.getComponent() instanceof ComponentProcessRender) {
            ComponentProcessRender component = (ComponentProcessRender) this.getComponent();
            text = ModUtils.getString(component.getProcess().getProgress(guiComponent.getIndex()) * 100) + "%";
        } else if (this.getComponent() instanceof CoolComponent) {
            CoolComponent component = (CoolComponent) this.getComponent();
            text =
                    ModUtils.getString(component
                            .getEnergy()) + "°C" + "/" + ModUtils.getString(component.getCapacity()) + "°C";
            if (component
                    .getEnergy() == 100) {
                text += "\n" + Localization.translate("iu.need_colling2");

            } else if (component
                    .getEnergy() >= 50 && component
                    .getEnergy() < 100) {
                text += "\n" + Localization.translate("iu.need_colling1");

            } else if (component
                    .getEnergy() < 50 && component
                    .getEnergy() > 10) {
                text += "\n" + Localization.translate("iu.need_colling");
            }

        } else if (this.getComponent() instanceof HeatComponent) {
            HeatComponent component = (HeatComponent) this.getComponent();
            text =
                    ModUtils.getString(component
                            .getEnergy()) + "°C" + "/" + ModUtils.getString(component.getCapacity()) + "°C";
            if (component.need) {
                text += "\n" + Localization.translate("iu.need_heat");
            }

        } else if (this.getComponent() instanceof FluidTank) {
            FluidTank component = (FluidTank) this.getComponent();
            String text1;
            try {
                text1 = component.getFluid().getLocalizedName();
            } catch (Exception e) {
                text1 = "";
            }

            text = "Fluid " + text1 + ": " +
                    ModUtils.getString(component
                            .getFluidAmount()) + "/" + ModUtils.getString(component.getCapacity());

        }


        cir.setReturnValue(text);
        cir.cancel();
    }
}
