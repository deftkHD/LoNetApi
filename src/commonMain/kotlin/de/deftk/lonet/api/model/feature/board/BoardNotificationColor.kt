package de.deftk.lonet.api.model.feature.board

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = BoardNotificationColorSerializer::class)
enum class BoardNotificationColor(val serialId: Int) {
    BLUE(0),
    GREEN(1),
    RED(2),
    YELLOW(3),
    WHITE(4);

    companion object {
        fun getById(id: Int?) = values().firstOrNull { it.serialId == id }
    }
}

class BoardNotificationColorSerializer: KSerializer<BoardNotificationColor?> {

    override fun deserialize(decoder: Decoder): BoardNotificationColor? {
        return BoardNotificationColor.getById(decoder.decodeInt())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("BoardNotificationColor", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: BoardNotificationColor?) {
        if (value != null)
            encoder.encodeInt(value.serialId)
    }
}