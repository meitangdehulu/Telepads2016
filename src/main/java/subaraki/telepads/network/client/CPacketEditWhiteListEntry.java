package subaraki.telepads.network.client;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import subaraki.telepads.network.IPacketBase;
import subaraki.telepads.network.NetworkHandler;
import subaraki.telepads.utility.ClientReferences;

public class CPacketEditWhiteListEntry implements IPacketBase {

    public String name;
    public UUID id;
    public boolean add;

    public CPacketEditWhiteListEntry() {

    }

    public CPacketEditWhiteListEntry(String name, UUID id, boolean add) {

        this.name = name;
        this.id = id;
        this.add = add;
    }

    public CPacketEditWhiteListEntry(PacketBuffer buf) {
        this.decode(buf);
    }

    @Override
    public void encode(PacketBuffer buf)
    {
        buf.writeString(name, 16);
        buf.writeBoolean(add);
        buf.writeUniqueId(id);

    }

    @Override
    public void decode(PacketBuffer buf)
    {
        this.name = buf.readString();
        this.add = buf.readBoolean();
        this.id = buf.readUniqueId();
    }

    @Override
    public void handle(Supplier<Context> context)
    {
        context.get().enqueueWork(() -> {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                ClientReferences.handlePacket(this);
            });
        });
        context.get().setPacketHandled(true);
    }

    @Override
    public void register(int id)
    {

        NetworkHandler.NETWORK.registerMessage(id, CPacketEditWhiteListEntry.class, CPacketEditWhiteListEntry::encode, CPacketEditWhiteListEntry::new,
                CPacketEditWhiteListEntry::handle);

    }

}
