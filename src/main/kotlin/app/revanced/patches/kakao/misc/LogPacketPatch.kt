package app.revanced.patches.kakao.misc

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.kakao.misc.fingerprints.LogReqPacketFingerprint
import app.revanced.patches.kakao.misc.fingerprints.LogResPacketFingerprint

@Patch(
    name = "Logging loco packets",
    compatiblePackages = [CompatiblePackage("com.kakao.talk", ["10.8.3", "10.9.0"])]
)
object LogPacketPatch : BytecodePatch(
    setOf(LogReqPacketFingerprint, LogResPacketFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        val reqMethod = LogReqPacketFingerprint.result!!.mutableMethod

        reqMethod.addInstructions(0,
            """
                const-string v0, "com.kakao.talk.locoreq"

                invoke-virtual {p1}, Ljava/lang/Object;->toString()Ljava/lang/String;

                move-result-object v1

                invoke-static {v0, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I
            """.trimIndent())

        val resMethod = LogResPacketFingerprint.result!!.mutableMethod

        resMethod.addInstructions(0,
            """
                const-string v0, "com.kakao.talk.locores"

                invoke-virtual {p1}, Ljava/lang/Object;->toString()Ljava/lang/String;

                move-result-object v1

                invoke-static {v0, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I
            """.trimIndent())
    }
}