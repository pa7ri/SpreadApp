# TFG

## Contributing : 
- Create new branch for the task:
  - Format := ID-'taskDescription'
    - ID from pivotalTracker : [projectSpread](https://www.pivotaltracker.com/n/projects/2208687)
  - Example : 
    ```
    161444291-AddInputOutputResponse 
    ```  
- Commit new changes and specify the purpose:
  - Format := purposeID\ ``commitDescription``
    - purposeID : 
      - **feat** for new feature 
      - **chore** for minor changes
      - **bug** for fixing bugs
  - Content := commit template
  - Example : 
    ```
    feat\addInputOutputInterface 
    ```  
- Before creating a new pull request, make sure master content is updated into the branch.
- Merge to *Master* if the PR got at least one approved response
