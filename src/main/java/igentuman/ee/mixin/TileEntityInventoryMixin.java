package igentuman.ee.mixin;

import com.denfop.componets.AbstractComponent;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import igentuman.ee.iucomponent.RFComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityInventory.class)
public abstract class  TileEntityInventoryMixin {

    @Shadow public abstract <T extends AbstractComponent> T addComponent(T component);

    @Inject(method = "addComponent", at = @At("TAIL"), remap=false)
    public <T extends AbstractComponent> void addComponent(T component, CallbackInfoReturnable<T> cir) {
        if(component instanceof AdvEnergy || component instanceof ComponentBaseEnergy ||
                component.getClass().toString().contains("TileSolarPanel")) {
            this.addComponent(new RFComponent((TileEntityInventory) (Object) this, component));
        }
    }
}
