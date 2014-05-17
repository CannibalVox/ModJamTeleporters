package net.modyssey.teleporters.network;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

import java.util.EnumMap;

@ChannelHandler.Sharable
public class ModysseyNetwork extends FMLIndexedMessageToMessageCodec<ModysseyPacket> {

    private static final ModysseyNetwork INSTANCE = new ModysseyNetwork();
    private static final EnumMap<Side, FMLEmbeddedChannel> channels = Maps.newEnumMap(Side.class);

    public static void init() {
        if (!channels.isEmpty())
            return;

        //TODO: add packet discriminators

        channels.putAll(NetworkRegistry.INSTANCE.newChannel("ModysseyTeleporters", INSTANCE));
    }

    public void encodeInto(ChannelHandlerContext ctx, ModysseyPacket msg, ByteBuf target) throws Exception {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        msg.write(out);
        target.writeBytes(out.toByteArray());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, ModysseyPacket msg) {
        ByteArrayDataInput in = ByteStreams.newDataInput(source.array());

        in.skipBytes(1);    //This is a packet ID that we already used to init the packet
        msg.read(in);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            handleClient(msg);
        else
            handleServer(ctx, msg);
    }

    @SideOnly(Side.CLIENT)
    private void handleClient(ModysseyPacket msg) {
        msg.handleClient(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer);
    }

    private void handleServer(ChannelHandlerContext ctx, ModysseyPacket msg) {
        EntityPlayerMP player = ((NetHandlerPlayServer)ctx)
    }
}
