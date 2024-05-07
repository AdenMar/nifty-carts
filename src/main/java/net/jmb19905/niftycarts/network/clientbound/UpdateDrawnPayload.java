package net.jmb19905.niftycarts.network.clientbound;

import net.jmb19905.niftycarts.NiftyCarts;
import net.jmb19905.niftycarts.entity.AbstractDrawnEntity;
import net.jmb19905.niftycarts.network.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public record UpdateDrawnPayload(int pullingId, int cartId) implements CustomPacketPayload {

    public static final Type<UpdateDrawnPayload> TYPE = CustomPacketPayload.createType(NiftyCarts.MOD_ID + ":update_drawn");
    public static final StreamCodec<FriendlyByteBuf, UpdateDrawnPayload> CODEC = new StreamCodec<FriendlyByteBuf, UpdateDrawnPayload>() {
        @Override
        public UpdateDrawnPayload decode(FriendlyByteBuf buf) {
            int pullingId = buf.readVarInt();
            int cartId = buf.readVarInt();
            return new UpdateDrawnPayload(pullingId, cartId);
        }

        @Override
        public void encode(FriendlyByteBuf buf, UpdateDrawnPayload payload) {
            buf.writeVarInt(payload.pullingId);
            buf.writeVarInt(payload.cartId);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateDrawnPayload msg, Level level) {
        final Entity e = level.getEntity(msg.cartId);
        if (e instanceof AbstractDrawnEntity drawn) {
            if (msg.pullingId < 0) {
                drawn.setPulling(null);
            } else {
                drawn.setPulling(level.getEntity(msg.pullingId));
            }
        }
    }
}
