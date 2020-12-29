describe('Timer', () => {
    context('Timer', () => {
        beforeEach(() => {
            cy.visit('http://localhost:3449')
            cy.get('#timer').click()

            cy.get('#elapsed-timer').as('elapsedTimer')
        })

        it('should show the elapsed time label', () => {
            cy.wait(2000)
            cy.get('@elapsedTimer').invoke('text').should('match', /2.*s$/)
        })
    })
})
