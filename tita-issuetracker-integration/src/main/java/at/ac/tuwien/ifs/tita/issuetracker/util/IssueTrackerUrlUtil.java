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
package at.ac.tuwien.ifs.tita.issuetracker.util;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;

public class IssueTrackerUrlUtil {
    
    /**
     * Composes an Url to get the Task-description of the IssueTrackerTask.
     * @param issueTrackerUrl - url to the issueTracker
     * @param projectName - Name of the IssueTracker-Project
     * @param taskId - Id of the IssueTrackerTask
     * @return - created url
     */
    public static String getUrl(Effort e){
        
        IssueTracker tracker = e.getIssueTTask().getIsstProject().getIssueTracker();
        String url = tracker.getUrl();
        String projectname = e.getIssueTTask().getIsstProject().getProjectName();
        Long taskno = e.getIssueTTask().getIsstTaskId();
        
        if(tracker.isMantisType()){
            return url + "id="+taskno;
        }
        throw new RuntimeException("IssueTrackerId " + tracker.getId() + " not specified "+
                "for URL-Generation.");
    }
    
}
