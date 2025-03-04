## 📝 Description
<!-- Provide a summary of the changes you made and why they are necessary. -->

## 🔗 Related Issues/Tickets
<!-- Link to relevant issues or Jira tickets (if applicable). -->
- Closes #ISSUE_NUMBER

## ✅ Checklist
- [ ] My code follows the repository's coding standards.
- [ ] I have written tests to cover my changes (if necessary).
- [ ] I have manually tested my feature in the `dev` environment.
- [ ] The feature is ready for staging deployment (`promote-to-stage` label required).
- [ ] I have updated documentation (if necessary).

## 🚀 Deployment Notes
<!-- Add any special deployment instructions (if needed). -->
- This PR affects **[list affected services]**.
- Ensure the database migrations are applied before deployment.

## 🖥️ Testing Instructions
<!-- Describe how reviewers can test your changes locally. -->
1. Verify that the following functionality works as expected:
    - [ ] Endpoint `/api/example` returns 200
    - [ ] Kafka event is correctly published

## 🏷️ Labels
<!-- Select the appropriate labels for automatic deployments -->
- [ ] `promote-to-stage`
- [ ] `promote-to-prod`

**PR Status:** 🚦 Ready for Review  
**Reviewer(s):** 👀 @mention_reviewer_here