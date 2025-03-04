package com.aseubel.infrastructure.adapter.repo;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.sfile.adapter.repo.IFileRepository;
import com.aseubel.domain.sfile.model.entity.SFileEntity;
import com.aseubel.domain.sfile.model.vo.CourseVO;
import com.aseubel.infrastructure.convertor.SFileConvertor;
import com.aseubel.infrastructure.dao.*;
import com.aseubel.infrastructure.dao.po.SFile;
import com.aseubel.infrastructure.redis.IRedisService;
import com.aseubel.types.util.AliOSSUtil;
import com.aseubel.types.util.RedisKeyBuilder;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static com.aseubel.types.common.Constant.COURSES_EXPIRE_TIME;
import static com.aseubel.types.common.Constant.REPEAT_DOWNLOAD_EXPIRE_TIME;

/**
 * @author Aseubel
 * @description 分享文件仓储层实现类
 * @date 2025-01-20 16:22
 */
@Repository
public class SFileRepository implements IFileRepository {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SFileMapper sFileMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private MajorMapper majorMapper;

    @Resource
    private SFileDownloadRecordMapper sFileDownloadRecordMapper;

    @Resource
    private SFileConvertor sFileConvertor;

    @Resource
    private AliOSSUtil aliOSSUtil;

    @Resource
    private IRedisService redisService;

    @Override
    public void saveSFile(SFileEntity file) {
        sFileMapper.addSFile(sFileConvertor.convert(file));
    }

    @Override
    public void deleteRepeatedSFile() {
        sFileMapper.deleteRepeatedSFile();
    }

    @Override
    public List<SFileEntity> listSFile(String fileId, Integer limit, String sortField, String courseName) {
        return Optional.ofNullable(StringUtils.isEmpty(fileId)
                        ? sFileMapper.listSFileAhead(limit, sortField, courseName)
                        : sFileMapper.listSFile(fileId, limit, sortField, courseName))
                .map(p -> p.stream().map(sFileConvertor::convert).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<SFileEntity> listSFileByTypeId(String fileId, String courseName, Integer limit) {
        return Optional.ofNullable(StringUtils.isEmpty(fileId)
                        ? sFileMapper.listSFileByTypeIdAhead(courseName, limit)
                        : sFileMapper.listSFileByTypeId(fileId, courseName, limit))
                .map(p -> p.stream().map(sFileConvertor::convert).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public void saveSFileDownloadRecord(String fileId, String userId) {
        sFileDownloadRecordMapper.saveDownloadRecord(fileId, userId);
    }

    @Override
    public void deleteMissingSFile() throws ClientException {
        List<SFile> fileRecords = sFileMapper.listAllSFile();
        Set<String> ossRecordSet = new HashSet<>(aliOSSUtil.listObjects());
        List<Long> missingFileIds = new ArrayList<>();
        // 找出数据库中存在但oss没有对应对象的记录
        for (SFile fileRecord : fileRecords) {
            if (!ossRecordSet.contains(fileRecord.getSfileUrl().substring(fileRecord.getSfileUrl().lastIndexOf("utopia/")))) {
                missingFileIds.add(fileRecord.getId());
            }
        }
        if (!CollectionUtil.isEmpty(missingFileIds)) {
            sFileMapper.deleteMissingSFile(missingFileIds);
        }
    }

    @Override
    public SFileEntity getSFileById(String fileId) {
        return sFileConvertor.convert(sFileMapper.getSFileBySFileId(fileId));
    }

    @Override
    public SFileEntity getSFileByUrl(String fileUrl) {
        return sFileConvertor.convert(sFileMapper.getSFileBySFileUrl(fileUrl));
    }

    @Override
    public void incrementDownloadCount(String userId, String fileId) {
        if (redisService.getValue(RedisKeyBuilder.FileRepeatDownloadKey(userId)) == null) {
            sFileMapper.incrementDownloadCount(fileId);
            redisService.setValue(RedisKeyBuilder.FileRepeatDownloadKey(userId), fileId, REPEAT_DOWNLOAD_EXPIRE_TIME);
        }
    }

    @Override
    public List<CourseVO> queryCourses() {
        List<CourseVO> courseVOS = redisService.getValue(RedisKeyBuilder.CoursesKey());
        if (CollectionUtil.isNotEmpty(courseVOS)) {
            return courseVOS;
        }

        List<String> majorNames = majorMapper.getAllMajor();
        courseVOS = new ArrayList<>();
        for (String majorName : majorNames) {
            CourseVO course = new CourseVO(majorName, courseMapper.listCourseNamesByMajorName(majorName));
            courseVOS.add(course);
        }

        redisService.setValue(RedisKeyBuilder.CoursesKey(), courseVOS, COURSES_EXPIRE_TIME);
        return courseVOS;
    }

}
