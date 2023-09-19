package com.zero.triptalk.plannerdetail;

import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerDetailService {

    private final PlannerDetailRepository plannerDetailRepository;
    private final UserRepository userRepository;
    public List<PlannerDetailResponse> getAllPlannerDetail() {

        List<PlannerDetail> detailList = plannerDetailRepository.findAll();

        return PlannerDetailResponse.of(detailList);
    }

    public boolean createPlannerDetail(Long planId, List<MultipartFile> files,
                                                            PlannerDetailRequest request) {

        // file 검증

        UserEntity user = userRepository.findById(request.getUserId()).orElseThrow(RuntimeException::new);

        PlannerDetail plannerDetail = PlannerDetail.builder()
                .plannerId(planId)
                .userId(request.getId())
                .date(request.getDate())
                .date(request.getDate())
                .time(request.getTime())
                .image(request.getImage())
                .location(request.getLocation())
                .description(request.getDescription())
                .build();
        plannerDetailRepository.save(plannerDetail);

        return true;
    }

    public boolean createPlannerDetailList(Long planId, List<MultipartFile> files, List<PlannerDetailRequest> requests) {

        // file 검증

        UserEntity user = userRepository.findById(requests.get(0).getUserId()).orElseThrow(RuntimeException::new);

        List<PlannerDetail> detailList = new ArrayList<>();
        for (PlannerDetailRequest x : requests) {
            PlannerDetail plannerDetail = PlannerDetail.builder()
                    .plannerId(planId)
                    .userId(x.getUserId())
                    .date(x.getDate())
                    .time(x.getTime())
                    .image(x.getImage())
                    .location(x.getLocation())
                    .description(x.getDescription())
                    .build();
            detailList.add(plannerDetail);
        }
        plannerDetailRepository.saveAll(detailList);

        return true;
    }

    public boolean updatePlannerDetail(Long planId, List<MultipartFile> files, PlannerDetailRequest request) {

        // file 검증

        UserEntity user = userRepository.findById(request.getUserId()).orElseThrow(RuntimeException::new);

        PlannerDetail plannerDetail = plannerDetailRepository.findById(request.getId()).orElseThrow(RuntimeException::new);

        plannerDetail.updatePlannerDetail(request);
        plannerDetailRepository.save(plannerDetail);

        return true;
    }

    public boolean deletePlannerDetail(Long planId, Long detailId, String email) {

        UserEntity user = userRepository.findByEmail(email).orElseThrow(RuntimeException::new);

        PlannerDetail plannerDetail = plannerDetailRepository.findById(detailId).orElseThrow(RuntimeException::new);

        if (!user.getUserId().equals(plannerDetail.getUserId())) {
            throw new RuntimeException();
        }

        plannerDetailRepository.deleteById(plannerDetail.getId());

        return true;
    }
}
