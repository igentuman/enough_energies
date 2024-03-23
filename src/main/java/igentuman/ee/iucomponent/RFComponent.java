package igentuman.ee.iucomponent;

import com.denfop.componets.AbstractComponent;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class RFComponent extends AbstractComponent {
    private AbstractComponent component;
    public RFComponent(TileEntityBlock parent, AbstractComponent component) {
        super(parent);
        this.component = component;
    }

    private boolean isActiveForSide(EnumFacing side) {
        if(component instanceof AdvEnergy)
            return ((AdvEnergy) component).getSourceDirs().contains(side) || ((AdvEnergy) component).getSinkDirs().contains(side);
        if(component instanceof ComponentBaseEnergy)
            return ((ComponentBaseEnergy) component).getSourceDirs().contains(side) || ((ComponentBaseEnergy) component).getSinkDirs().contains(side);
        if(isSolarPanel())
            return side != EnumFacing.UP;
        return false;
    }

    private boolean isSolarPanel() {
        return component.getClass().toString().contains("TileSolarPanel");
    }

    @Override
    public Collection<? extends Capability<?>> getProvidedCapabilities(EnumFacing side) {
        if(isActiveForSide(side) || side == null) {
            return Collections.singleton(CapabilityEnergy.ENERGY);
        }
        return super.getProvidedCapabilities(side);
    }

    protected HashMap<EnumFacing, IEnergyStorage> capsMap = new HashMap<>();

    @Override
    public <T> T getCapability(Capability<T> cap, EnumFacing side) {
        if(cap == CapabilityEnergy.ENERGY) {
            if(isActiveForSide(side) || side == null) {
                if(!capsMap.containsKey(side)) {
                    capsMap.put(side, new RFEnergyStorage(side, component));
                }
                return (T) capsMap.get(side);
            }
        }
        return super.getCapability(cap, side);
    }

    public static class RFEnergyStorage implements IEnergyStorage {
        private EnumFacing side;
        private AbstractComponent component;

        public RFEnergyStorage(EnumFacing side, AbstractComponent component) {
            this.side = side;
            this.component = component;
        }

        public int receiveEnergy(int maxReceive, boolean simulate) {
            if(simulate) {
                return maxReceive;
            }
            if(component instanceof AdvEnergy)
                return (int) (((AdvEnergy) component).addEnergy((double) maxReceive / 4)) * 4;
            if(component instanceof ComponentBaseEnergy)
                return (int) (((ComponentBaseEnergy) component).addEnergy((double) maxReceive / 4)) * 4;
            if(isSolarPanel(component)) {
                return 0;
            }
            return 0;
        }

        private boolean isSolarPanel(AbstractComponent component) {
            return component.getClass().toString().contains("TileSolarPanel");
        }

        public int extractEnergy(int maxExtract, boolean simulate) {
            if(component instanceof AdvEnergy)
                return (int) (((AdvEnergy) component).useEnergy((double) maxExtract / 4, simulate)) * 4;
            if(component instanceof ComponentBaseEnergy)
                return (int) (((ComponentBaseEnergy) component).useEnergy((double) maxExtract / 4, simulate)) * 4;
            if(isSolarPanel(component)) {
                TileSolarPanel panel = (TileSolarPanel) component.getParent();
                double toExtract = Math.min(panel.canExtractEnergy(), maxExtract / 4);
                if(simulate) return (int) toExtract * 4;
                panel.extractEnergy(toExtract);
            }
            return  0;
        }

        public int getEnergyStored() {
            if(component instanceof AdvEnergy)
                return (int) (((AdvEnergy) component).getEnergy() * 4);
            if(component instanceof ComponentBaseEnergy)
                return (int) (((ComponentBaseEnergy) component).getEnergy() * 4);
            if(isSolarPanel(component)) {
                TileSolarPanel panel = (TileSolarPanel) component.getParent();
                return (int) (panel.canExtractEnergy() * 4);
            }
            return 0;
        }

        public int getMaxEnergyStored() {
            if(component instanceof AdvEnergy)
                return (int) (((AdvEnergy) component).getCapacity() * 4);
            if(component instanceof ComponentBaseEnergy)
                return (int) (((ComponentBaseEnergy) component).getCapacity() * 4);
            if(isSolarPanel(component)) {
                TileSolarPanel panel = (TileSolarPanel) component.getParent();
                return (int) (panel.maxStorage * 4);
            }
            return 0;
        }

        public boolean canExtract() {
            if(component instanceof AdvEnergy)
                return (((AdvEnergy) component).getSourceDirs().contains(side));
            if(component instanceof ComponentBaseEnergy)
                return (((ComponentBaseEnergy) component).getSourceDirs().contains(side));
            if(isSolarPanel(component)) {
                return true;
            }
            return false;
        }

        public boolean canReceive() {
            if(component instanceof AdvEnergy)
                return (((AdvEnergy) component).getSinkDirs().contains(side));
            if(component instanceof ComponentBaseEnergy)
                return (((ComponentBaseEnergy) component).getSinkDirs().contains(side));
            return false;
        }
    }

}
