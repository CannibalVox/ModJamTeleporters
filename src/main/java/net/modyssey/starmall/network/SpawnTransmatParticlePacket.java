package net.modyssey.starmall.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.modyssey.starmall.tileentities.TileEntityTeleporterController;
import net.modyssey.starmall.tileentities.io.PadData;

import java.util.Random;

public class SpawnTransmatParticlePacket extends ModysseyPacket {
    int x;
    int y;
    int z;

    public SpawnTransmatParticlePacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SpawnTransmatParticlePacket() {}

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
    }

    @Override
    public void read(ByteArrayDataInput in) {
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        TileEntity te = world.getTileEntity(x, y, z);

        if (te != null && te instanceof TileEntityTeleporterController) {
            TileEntityTeleporterController controller = (TileEntityTeleporterController)te;

            Random rand = world.rand;
            for (int i = 0; i < controller.getPadCount(); i++) {
                PadData pad = controller.getPadData(i);

                for (int j = 0; j < 25; j++) {
                    world.spawnParticle("portal", pad.getPadXCoord() + rand.nextDouble(), pad.getPadYCoord() + 1.0 + (rand.nextDouble() * 0.6) - 0.3, pad.getPadZCoord() + rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
                }
            }
        }
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        //Shouldn't be here
    }
}
