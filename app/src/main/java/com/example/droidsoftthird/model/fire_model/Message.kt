package com.example.droidsoftthird.model.fire_model
import com.google.firebase.firestore.DocumentId
import java.util.*

interface Message{
    val userId:String?
    val userName:String?
    val userImageUrl:String?
    val messageType:Double?
    val timestamp: Date?
    val messageId:String?
}

//0.0
data class TextMessage(
    override val userId: String? = null,
    override val userName: String? = null,
    override val userImageUrl: String? = null,
    override val messageType: Double? = null,
    val text: String? = null,
    override val timestamp: Date? = null,
    @DocumentId
    override val messageId: String? = null,

): Message

//1.0
data class ImageMessage(
    override val userId: String? = null,
    override val userName: String? = null,
    override val userImageUrl: String? = null,
    override val messageType: Double? = null,
    val imageRef: String? = null,
    override val timestamp: Date? = null,
    @DocumentId
    override val messageId: String? = null,
): Message

//2.0
data class FileMessage(
    override val userId: String? = null,
    override val userName: String? = null,
    override val userImageUrl: String? = null,
    override val messageType: Double? = null,
    val fileName:String? = null,
    val fileRef:String? = null,
    override val timestamp: Date? = null,
    @DocumentId
    override val messageId: String? = null,

): Message

//3.0
data class RecordMessage(
    override val userId: String? = null,
    override val userName: String? = null,
    override val userImageUrl: String? = null,
    override val messageType: Double? = null,
    val voiceRef:String? = null,
    var duration:String? = null,
    var currentProgress: String? = null,
    var isPlaying:Boolean? = null,
    override val timestamp: Date? = null,
    @DocumentId
    override val messageId: String? = null,

): Message

