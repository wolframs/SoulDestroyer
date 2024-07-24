package souldestroyer.raydium.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import souldestroyer.raydium.model.RaydiumMintInfo
import souldestroyer.raydium.model.RaydiumPool
import souldestroyer.raydium.model.RaydiumPoolDayStats
import souldestroyer.raydium.model.RaydiumPoolMonthStats
import souldestroyer.raydium.model.RaydiumPoolWeekStats
import souldestroyer.raydium.model.RaydiumRewardDefaultInfo

object RaydiumPoolSerializer : KSerializer<RaydiumPool> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RaydiumPool") {
        element<String>("type")
        element<String>("programId")
        element<String>("id")
        element<RaydiumMintInfo>("mintA")
        element<RaydiumMintInfo>("mintB")
        element<Double>("price")
        element<Double>("mintAmountA")
        element<Double>("mintAmountB")
        element<Double>("feeRate")
        element<String>("openTime")
        element<Double>("tvl")
        element<RaydiumPoolDayStats>("day")
        element<RaydiumPoolWeekStats>("week")
        element<RaydiumPoolMonthStats>("month")
        element<List<String>>("pooltype")
        element<List<RaydiumRewardDefaultInfo>>("rewardDefaultInfos", isOptional = true)
        element<Int>("farmUpcomingCount")
        element<Int>("farmOngoingCount")
        element<Int>("farmFinishedCount")
        element<String?>("marketId", isOptional = true)
        element<RaydiumMintInfo>("lpMint")
        element<Double>("lpPrice")
        element<Double>("lpAmount")
    }

    override fun deserialize(decoder: Decoder): RaydiumPool {
        val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException("Expected JsonDecoder")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        return RaydiumPool(
            type = jsonObject.getValue("type").jsonPrimitive.content,
            programId = jsonObject.getValue("programId").jsonPrimitive.content,
            id = jsonObject.getValue("id").jsonPrimitive.content,
            mintA = jsonDecoder.json.decodeFromJsonElement(RaydiumMintInfo.serializer(), jsonObject.getValue("mintA")),
            mintB = jsonDecoder.json.decodeFromJsonElement(RaydiumMintInfo.serializer(), jsonObject.getValue("mintB")),
            price = jsonObject.getValue("price").jsonPrimitive.double,
            mintAmountA = jsonObject.getValue("mintAmountA").jsonPrimitive.double,
            mintAmountB = jsonObject.getValue("mintAmountB").jsonPrimitive.double,
            feeRate = jsonObject.getValue("feeRate").jsonPrimitive.double,
            openTime = jsonObject.getValue("openTime").jsonPrimitive.content,
            tvl = jsonObject.getValue("tvl").jsonPrimitive.double,
            day = jsonDecoder.json.decodeFromJsonElement(RaydiumPoolDayStats.serializer(), jsonObject.getValue("day")),
            week = jsonDecoder.json.decodeFromJsonElement(RaydiumPoolWeekStats.serializer(), jsonObject.getValue("week")),
            month = jsonDecoder.json.decodeFromJsonElement(RaydiumPoolMonthStats.serializer(), jsonObject.getValue("month")),
            pooltype = jsonDecoder.json.decodeFromJsonElement(ListSerializer(String.serializer()), jsonObject.getValue("pooltype")),
            raydiumRewardDefaultInfos = jsonObject["rewardDefaultInfos"]?.let {
                jsonDecoder.json.decodeFromJsonElement(ListSerializer(RaydiumRewardDefaultInfo.serializer()), it)
            } ?: emptyList(),
            farmUpcomingCount = jsonObject.getValue("farmUpcomingCount").jsonPrimitive.int,
            farmOngoingCount = jsonObject.getValue("farmOngoingCount").jsonPrimitive.int,
            farmFinishedCount = jsonObject.getValue("farmFinishedCount").jsonPrimitive.int,
            marketId = jsonObject["marketId"]?.jsonPrimitive?.contentOrNull,
            lpMint = jsonDecoder.json.decodeFromJsonElement(RaydiumMintInfo.serializer(), jsonObject.getValue("lpMint")),
            lpPrice = jsonObject.getValue("lpPrice").jsonPrimitive.double,
            lpAmount = jsonObject.getValue("lpAmount").jsonPrimitive.double
        )
    }

    override fun serialize(encoder: Encoder, value: RaydiumPool) {
        // Implement if needed for serialization, otherwise leave it empty
    }
}