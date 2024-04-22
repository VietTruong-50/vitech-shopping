package vn.vnpt.api.dto.mapper.task;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import vn.vnpt.api.dto.out.task.TaskDetailOut;
import vn.vnpt.api.dto.out.task.TaskListOut;
import vn.vnpt.api.dto.proc.out.TaskDetailProcOut;
import vn.vnpt.api.dto.proc.out.TaskListProcOut;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-01T15:54:47+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class TaskMapperOutImpl implements TaskMapperOut {

    @Override
    public List<TaskListOut> toListTaskListOut(List<TaskListProcOut> taskListProcOuts) {
        if ( taskListProcOuts == null ) {
            return null;
        }

        List<TaskListOut> list = new ArrayList<TaskListOut>( taskListProcOuts.size() );
        for ( TaskListProcOut taskListProcOut : taskListProcOuts ) {
            list.add( taskListProcOutToTaskListOut( taskListProcOut ) );
        }

        return list;
    }

    @Override
    public TaskDetailOut toTaskDetailOut(TaskDetailProcOut taskDetailProcOut) {
        if ( taskDetailProcOut == null ) {
            return null;
        }

        TaskDetailOut taskDetailOut = new TaskDetailOut();

        taskDetailOut.setTaskId( taskDetailProcOut.getTaskId() );
        taskDetailOut.setName( taskDetailProcOut.getName() );
        taskDetailOut.setTaskType( taskDetailProcOut.getTaskType() );
        if ( taskDetailProcOut.getRetries() != null ) {
            taskDetailOut.setRetries( taskDetailProcOut.getRetries().intValue() );
        }
        if ( taskDetailProcOut.getRetryDelay() != null ) {
            taskDetailOut.setRetryDelay( taskDetailProcOut.getRetryDelay().intValue() );
        }
        taskDetailOut.setTimeUnit( taskDetailProcOut.getTimeUnit() );
        if ( taskDetailProcOut.getExecutionTimeout() != null ) {
            taskDetailOut.setExecutionTimeout( taskDetailProcOut.getExecutionTimeout().intValue() );
        }
        taskDetailOut.setTriggerRule( taskDetailProcOut.getTriggerRule() );
        taskDetailOut.setCatalogId( taskDetailProcOut.getCatalogId() );
        taskDetailOut.setFilePath( taskDetailProcOut.getFilePath() );

        return taskDetailOut;
    }

    protected TaskListOut taskListProcOutToTaskListOut(TaskListProcOut taskListProcOut) {
        if ( taskListProcOut == null ) {
            return null;
        }

        TaskListOut taskListOut = new TaskListOut();

        taskListOut.setTaskId( taskListProcOut.getTaskId() );
        taskListOut.setName( taskListProcOut.getName() );

        return taskListOut;
    }
}
