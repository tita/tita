/**
   Copyright 2009 TiTA Project, Vienna University of Technology
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
   
       http://www.apache.org/licenses/LICENSE\-2.0
       
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
  
 */
package at.ac.tuwien.ifs.tita.business.service.tasks;

import java.util.List;
import java.util.Map;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.issue.service.IIssueTrackerService;
import at.ac.tuwien.ifs.tita.issuetracker.issue.service.IssueTrackerService;

public class TaskService implements ITaskService {

    /** {@inheritDoc} */
    @Override
    public IssueTrackerTask saveIssueTrackerTask(IssueTrackerTask issueTrackerTask) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public TiTATask saveTiTATask(TiTATask titaTask) {
        // TODO Auto-generated method stub
        return null;
    }

    public void test(List<IssueTrackerLogin> logins) throws ProjectNotFoundException {
        
        for (int i = 0; i < logins.size(); i++) {
            IIssueTrackerService issueTrackerService = new IssueTrackerService(logins.get(i));
            
            issueTrackerService.getIssueTrackerTasksByProjectId(10L);
        }
    }

    @Override
    public Map<Long, ITaskTrackable> getIssueTrackerTasks(TiTAProject project)
            throws ProjectNotFoundException {
        
        
        for(IssueTrackerProject issueTrackerProject : project.getIssueTrackerProjects()) {

        }
        return null;
    }

    @Override
    public Map<Long, TiTATask> getTiTATasks(TiTAProject project) throws ProjectNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

}
