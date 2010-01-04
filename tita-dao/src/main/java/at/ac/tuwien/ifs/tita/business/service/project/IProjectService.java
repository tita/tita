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
package at.ac.tuwien.ifs.tita.business.service.project;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.entity.TiTAProject;

/**
 * IProjectService encapsulates all tita project based db operations.
 * 
 * @author herbert
 * 
 */
@Transactional
public interface IProjectService {
    
    /**
     * Returns a list of all existing tita projects.
     * @return List of TiTAProjects
     */
    List<TiTAProject> findAllTiTAProjects();
    
//    /**
//     * Returns a list of all existing tita projects.
//     * @return List of TiTAProjects
//     */
//    List<TiTAProject> findAllTiTAProjects();
}
