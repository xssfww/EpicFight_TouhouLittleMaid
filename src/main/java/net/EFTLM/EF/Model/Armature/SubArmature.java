package net.EFTLM.EF.Model.Armature;

import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import javax.annotation.Nullable;
import java.util.Map;
public class SubArmature extends Armature {
    public @Nullable Armature parent;
    public void setupParent(Armature parent) {
        this.parent = parent;
    }
    public SubArmature(String name, int jointNumber, Joint rootJoint, Map<String, Joint> jointMap) {
        super(name, jointNumber, rootJoint, jointMap);
    }
    @Override
    public void setPose(Pose pose) {
        getPoseTransform(this.rootJoint, OpenMatrix4f.IDENTITY, pose, this.getPoseMatrices(), false);
    }
    private void getPoseTransform(Joint joint, OpenMatrix4f parentTransform, Pose pose, OpenMatrix4f[] jointMatrices, boolean applyOriginTransform) {
        String jtName = joint.getName();
        Joint subJt = searchJointByName(jtName);
        int subId = subJt.getId();
        OpenMatrix4f result;
        if (parent != null && parent.hasJoint(jtName)) {
            Joint parentJt = parent.searchJointByName(jtName);
            result = pose.orElseEmpty(jtName).getAnimationBoundMatrix(parentJt, parentTransform);
        } else {
            result = pose.orElseEmpty(jtName).getAnimationBoundMatrix(subJt, parentTransform);
        }
        jointMatrices[subId] = result;
        for (Joint jt : subJt.getSubJoints()) {
            getPoseTransform(jt, result, pose, jointMatrices, applyOriginTransform);
        }
        if (applyOriginTransform) {
            result.mulBack(joint.getToOrigin());
        }
    }
}
