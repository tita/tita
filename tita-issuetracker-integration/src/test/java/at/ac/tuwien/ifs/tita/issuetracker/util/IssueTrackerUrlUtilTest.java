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

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;

/**
 * Class for testing IssueTrackerUrlUtil.
 * @author Karin
 *
 */
public class IssueTrackerUrlUtilTest {

    /**
     * Tests the correct creation of a ManitsUrl.
     */
    @Test
    public void testGetManitsUrl(){
        IssueTracker issueTracker = mock(IssueTracker.class);
        when(issueTracker.getUrl()).thenReturn("www.mantis.com");
        when(issueTracker.getId()).thenReturn(1L);
        
        IssueTrackerProject project = mock(IssueTrackerProject.class);
        when(project.getIssueTracker()).thenReturn(issueTracker);
        when(project.getProjectName()).thenReturn("unused");
        
        IssueTrackerTask task = mock(IssueTrackerTask.class);
        when(task.getIsstProject()).thenReturn(project);
        when(task.getIsstTaskId()).thenReturn(2L);
        
        
        Effort e = mock(Effort.class);
        when(e.getIssueTTask()).thenReturn(task);
        
        assertEquals("www.mantis.com/view.php?id=2", IssueTrackerUrlUtil.getUrl(e));
        
       
    }
}
