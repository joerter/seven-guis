describe('Flight Booker', () => {
    context('Flight Booker', () => {
        const oneWayFlight = 'one-way flight'
        const returnFlight = 'return flight'

        beforeEach(() => {
            cy.visit('http://localhost:3449')
            cy.get('#flight-booker')
                .click()

            cy.get('select').as('tripSelect')
            cy.get('#start-date').as('startDate')
            cy.get('#end-date').as('endDate')
            cy.get('input[type=submit]').as('submitButton')
        })

        it('should enable end date iff return flight is selected', () => {
            cy.get('@tripSelect').should('have.value', oneWayFlight)
            cy.get('@endDate').should('have.attr', 'disabled')

            cy.get('@tripSelect').select(returnFlight)
            cy.get('@endDate').should('not.have.attr', 'disabled')
        })

        it('should disable the Book button when end date is changed to be before start date', () => {
            cy.get('@tripSelect').select(returnFlight)
            cy.get('@startDate').clear().type('2020/12/25')
            cy.get('@endDate').clear().type('2020/12/24')

            cy.get('@submitButton').should('have.attr', 'disabled')
        })

        it('should disable the Book button when start date is changed to be after start date', () => {
            cy.get('@tripSelect').select(returnFlight)
            cy.get('@endDate').clear().type('2020/12/25')
            cy.get('@startDate').clear().type('2020/12/26')

            cy.get('@submitButton').should('have.attr', 'disabled')
        })

        it('should enable the Book button when one-way flight is selected and start date is valid', () => {
            cy.get('@startDate').clear().type('hi')
            cy.get('@submitButton').should('have.attr', 'disabled')

            cy.get('@startDate').clear().type('10/14/2020')
            cy.get('@submitButton').should('have.not.attr', 'disabled')
        })

        it('should disable the Book button and set a red background color when start date is not a valid date', () => {
            cy.get('@startDate').clear().type('invalid date')

            cy.get('@submitButton').should('have.attr', 'disabled')
            cy.get('@startDate').should('have.class', 'invalid')
        })

        it('should disable the Book button and set a red background color when end date is not a valid date', () => {
            cy.get('@tripSelect').select(returnFlight)
            cy.get('@endDate').clear().type('invalid date')

            cy.get('@submitButton').should('have.attr', 'disabled')
            cy.get('@endDate').should('have.class', 'invalid')
        })

        it('should display a confirmation for a one way flight', () => {
            const startDate = '2020/12/25'
            cy.get('@startDate').clear().type(startDate)
            cy.get('@submitButton').click()

            cy.get('#confirmation-message').should('have.text',
                `You have booked a one-way flight on ${startDate}`)
        })

        it('should display a confirmation for a two way flight', () => {
            const startDate = '2020/12/25'
            const endDate = '2021/01/01'
            cy.get('@tripSelect').select(returnFlight)
            cy.get('@startDate').clear().type(startDate)
            cy.get('@endDate').clear().type(endDate)
            cy.get('@submitButton').click()

            cy.get('#confirmation-message').should('have.text',
               `You have booked a two-way flight starting on ${startDate} and returning on ${endDate}`)
        })
    })
})
