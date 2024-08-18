package app.revanced.patches.kakao.message

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patcher.util.proxy.mutableTypes.MutableField.Companion.toMutable
import app.revanced.patches.kakao.message.fingerprints.SetChatTypeMethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.immutable.ImmutableField

@Patch(
    name = "Set that chat is deleted or hidden",
    compatiblePackages = [CompatiblePackage("com.kakao.talk", ["10.8.3", "10.9.0"])]
)
object HookSetChatTypePatch : BytecodePatch(
    setOf(SetChatTypeMethodFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        val method = SetChatTypeMethodFingerprint.result!!.mutableMethod

        SetChatTypeMethodFingerprint.result!!.mutableClass.fields.add(
            ImmutableField(
                SetChatTypeMethodFingerprint.result!!.mutableMethod.definingClass,
                "isRealDeleted",
                "Z",
                AccessFlags.PUBLIC.value,
                null,
                null,
                null
            ).toMutable()
        )

        SetChatTypeMethodFingerprint.result!!.mutableClass.fields.add(
            ImmutableField(
                SetChatTypeMethodFingerprint.result!!.mutableMethod.definingClass,
                "isHidden",
                "Z",
                AccessFlags.PUBLIC.value,
                null,
                null,
                null
            ).toMutable()
        )

        SetChatTypeMethodFingerprint.result!!.mutableClass.fields.add(
            ImmutableField(
                SetChatTypeMethodFingerprint.result!!.mutableMethod.definingClass,
                "realType",
                "I",
                AccessFlags.PUBLIC.value,
                null,
                null,
                null
            ).toMutable()
        )

        val definingClassName = SetChatTypeMethodFingerprint.result!!.mutableMethod.definingClass

        method.addInstructions(0, """
            iput p1, p0, $definingClassName->realType:I
            
            const/16 v0, 0x4000
            if-ge v0, p1, :cond_set_field
            
            and-int/lit16 p1, p1, 0x4000
            if-eqz p1, :cond_is_hidden
            
            const/4 v0, 0x1
            iput-boolean v0, p0, $definingClassName->isRealDeleted:Z
            goto :cond_set_field
            
            :cond_is_hidden
            const/4 v0, 0x1
            iput-boolean v0, p0, $definingClassName->isHidden:Z
            
            :cond_set_field
            # set parameter_0 to field realType
            iget p1, p0, $definingClassName->realType:I
        """.trimIndent())
    }
}