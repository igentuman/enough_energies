package igentuman.ee.mixin;

import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.gui.Area;
import com.denfop.container.ContainerElectricBlock;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiElectricBlock;
import com.denfop.utils.ModUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiElectricBlock.class)
public abstract class IuGuiElectricBlockMixin extends GuiCore<ContainerElectricBlock> {

    @Final
    @Shadow private ContainerElectricBlock container;
    @Final
    @Shadow private String armorInv;
    @Final
    @Shadow private String name;

    public IuGuiElectricBlockMixin(ContainerElectricBlock container) {
        super(container);
    }

    @Shadow protected abstract void handleUpgradeTooltip(int par1, int par2);

    @Inject(method = "drawForegroundLayer", at = @At("HEAD"), cancellable = true, remap=false)
    public void drawForegroundLayer(int par1, int par2, CallbackInfo ci) {
        ci.cancel();
        fontRenderer.drawString(name, (xSize - fontRenderer.getStringWidth(name)) / 2, 6,
                4210752
        );
        fontRenderer.drawString(this.armorInv, 8, ySize - 126 + 3, 4210752);
        String tooltip =
                "RF: " + ModUtils.getString(this.container.base.energy.getEnergy() * 4) + "/" + ModUtils.getString(this.container.base.energy.getCapacity() * 4);
        new Area(this, 85 - 3, 38, 108 - 82, 46 - 38).withTooltip(tooltip).drawForeground(par1, par2);


        String output = Localization.translate(
                "EUStorage.gui.info.output",
                ModUtils.getString(EnergyNetGlobal.instance.getPowerFromTier(this.container.base.energy.getSourceTier()) * 4
                )
        );
        fontRenderer.drawString(output, 85, 70, 4210752);

        handleUpgradeTooltip(par1, par2);
    }

}
