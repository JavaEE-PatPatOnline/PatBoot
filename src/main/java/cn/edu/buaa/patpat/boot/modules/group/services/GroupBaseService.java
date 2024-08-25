package cn.edu.buaa.patpat.boot.modules.group.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.Group;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupMember;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupFilterMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupMemberMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupMemberView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

public abstract class GroupBaseService extends BaseService {
    @Autowired
    protected GroupMapper groupMapper;
    @Autowired
    protected GroupFilterMapper groupFilterMapper;
    @Autowired
    protected GroupMemberMapper groupMemberMapper;
    @Autowired
    protected BucketApi bucketApi;

    public Group getGroup(int id) {
        Group group = groupMapper.find(id);
        if (group == null) {
            throw new NotFoundException(M("group.exists.not"));
        }
        return group;
    }

    public GroupView getGroup(int id, GroupConfig config) {
        GroupView view = groupFilterMapper.findGroup(id);
        if (view == null) {
            throw new NotFoundException(M("group.exists.not"));
        }
        view.setMembers(getMembersInGroup(id));
        view.setMaxSize(config.getMaxSize());
        return view;
    }

    public GroupView findGroup(int id, GroupConfig config) {
        GroupView view = groupFilterMapper.findGroup(id);
        if (view != null) {
            view.setMembers(getMembersInGroup(id));
            view.setMaxSize(config.getMaxSize());
        }
        return view;
    }

    public GroupMember findMember(int courseId, int accountId) {
        return groupMemberMapper.find(courseId, accountId);
    }

    public List<GroupMemberView> getMembersInGroup(int groupId) {
        var members = groupFilterMapper.findMemberViewsInGroup(groupId);
        members.forEach(member -> member.setAvatar(
                bucketApi.recordToUrl(member.getAvatar())));
        return members;
    }
}