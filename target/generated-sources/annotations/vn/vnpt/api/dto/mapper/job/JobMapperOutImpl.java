package vn.vnpt.api.dto.mapper.job;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import vn.vnpt.api.dto.out.job.JobDetailOut;
import vn.vnpt.api.dto.out.job.JobListOut;
import vn.vnpt.api.dto.proc.out.JobDetailProcOut;
import vn.vnpt.api.dto.proc.out.JobListProcOut;
import vn.vnpt.common.model.PagingOut;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-01T15:54:47+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class JobMapperOutImpl implements JobMapperOut {

    @Override
    public PagingOut<JobListOut> toPagingJobListOut(PagingOut<JobListProcOut> jobListProcOutPagingOut) {
        if ( jobListProcOutPagingOut == null ) {
            return null;
        }

        PagingOut<JobListOut> pagingOut = new PagingOut<JobListOut>();

        pagingOut.setPage( jobListProcOutPagingOut.getPage() );
        pagingOut.setMaxSize( jobListProcOutPagingOut.getMaxSize() );
        pagingOut.setTotalElement( jobListProcOutPagingOut.getTotalElement() );
        pagingOut.setTotalPages( jobListProcOutPagingOut.getTotalPages() );
        pagingOut.setSort( jobListProcOutPagingOut.getSort() );
        pagingOut.setPropertiesSort( jobListProcOutPagingOut.getPropertiesSort() );
        pagingOut.setData( jobListProcOutListToJobListOutList( jobListProcOutPagingOut.getData() ) );

        return pagingOut;
    }

    @Override
    public JobDetailOut toJobDetailOut(JobDetailProcOut jobDetailOut) {
        if ( jobDetailOut == null ) {
            return null;
        }

        JobDetailOut jobDetailOut1 = new JobDetailOut();

        jobDetailOut1.setJobId( jobDetailOut.getJobId() );
        jobDetailOut1.setName( jobDetailOut.getName() );
        jobDetailOut1.setStatus( jobDetailOut.getStatus() );
        jobDetailOut1.setCreatedDate( jobDetailOut.getCreatedDate() );
        jobDetailOut1.setUserCreated( jobDetailOut.getUserCreated() );
        jobDetailOut1.setDescription( jobDetailOut.getDescription() );
        jobDetailOut1.setJobType( jobDetailOut.getJobType() );
        if ( jobDetailOut.getJobPeriod() != null ) {
            jobDetailOut1.setJobPeriod( jobDetailOut.getJobPeriod().intValue() );
        }
        jobDetailOut1.setJobTime( jobDetailOut.getJobTime() );
        jobDetailOut1.setJobDay( jobDetailOut.getJobDay() );
        jobDetailOut1.setJobTimeZone( jobDetailOut.getJobTimeZone() );
        jobDetailOut1.setScheduledInterval( jobDetailOut.getScheduledInterval() );

        return jobDetailOut1;
    }

    protected JobListOut jobListProcOutToJobListOut(JobListProcOut jobListProcOut) {
        if ( jobListProcOut == null ) {
            return null;
        }

        JobListOut jobListOut = new JobListOut();

        jobListOut.setJobId( jobListProcOut.getJobId() );
        jobListOut.setName( jobListProcOut.getName() );
        jobListOut.setStatus( jobListProcOut.getStatus() );
        jobListOut.setDagId( jobListProcOut.getDagId() );
        jobListOut.setUserCreated( jobListProcOut.getUserCreated() );
        jobListOut.setCreatedDate( jobListProcOut.getCreatedDate() );

        return jobListOut;
    }

    protected List<JobListOut> jobListProcOutListToJobListOutList(List<JobListProcOut> list) {
        if ( list == null ) {
            return null;
        }

        List<JobListOut> list1 = new ArrayList<JobListOut>( list.size() );
        for ( JobListProcOut jobListProcOut : list ) {
            list1.add( jobListProcOutToJobListOut( jobListProcOut ) );
        }

        return list1;
    }
}
