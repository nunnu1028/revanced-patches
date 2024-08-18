package app.revanced.patches.kakao.misc

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.getInstructions
import app.revanced.patcher.extensions.InstructionExtensions.removeInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.kakao.misc.fingerprints.ShopTabFingerprint
import app.revanced.util.getReference
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.reference.Reference

@Patch(
    name = "Remove Shop Tab patch",
    compatiblePackages = [CompatiblePackage("com.kakao.talk", ["10.8.3", "10.9.0"])]
)
object RemoveShopTabPatch : BytecodePatch(
    setOf(ShopTabFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        val method = ShopTabFingerprint.result!!.mutableMethod

        val shopTabInstruction = method.getInstructions().filter {
            it.opcode === Opcode.SGET_OBJECT && it.getReference<Reference>().toString().contains("SHOPPING_TAB")
        }

        val addShopTabInstruction = method.getInstructions().find {
            it.opcode === Opcode.INVOKE_VIRTUAL && it.location.index > shopTabInstruction[0].location.index
        }

        method.removeInstruction(addShopTabInstruction!!.location.index)
    }
}