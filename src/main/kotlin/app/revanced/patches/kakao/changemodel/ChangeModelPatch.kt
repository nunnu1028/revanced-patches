package app.revanced.patches.kakao.changemodel

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patcher.patch.options.PatchOption.PatchExtensions.stringPatchOption
import app.revanced.patches.kakao.changemodel.fingerprints.ChangeModelFingerprint

@Patch(
    name = "Change model patch",
    compatiblePackages = [CompatiblePackage("com.kakao.talk", ["10.8.3", "10.9.0"])],
    use = false
)
object CheckingSignPatch : BytecodePatch(
    setOf(ChangeModelFingerprint)
) {
    private val changeModelOption = stringPatchOption("model", "SM-G975N")

    override fun execute(context: BytecodeContext) {
        val optionValue = changeModelOption.value!!
        val method = ChangeModelFingerprint.result!!.mutableMethod
        method.addInstructions(
            0,
            """
                const-string v0, "$optionValue"
                return-object v0
            """.trimIndent()
        )
    }
}