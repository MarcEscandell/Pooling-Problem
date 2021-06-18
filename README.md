# Pooling problem
The pooling problem is a nonlinear network flow problem that models the operation of the supply chain of petrochemicals where crude oils are mixed to produce intermediate quality streams that are blended at retail locations to produce the final products.

In the following repository we explore different linearization techniques based on the paper located at `docs/[Gounaris_Misener_Floudas_2009_Comparison_Piecewise−Linear Relaxations_Pooling.pdf]`.

Currently, only 2 techniques have been implemented:
* Mc. Cormick envelopes
* Big M

### Technical details
The model has been developed in the **java** language following strong OOP (object-oriented programming) approach.

The optimization solver leverages on multiple _open-sourced_ packages that are interfaced using **Google OR-Tools** library.

To ease its deployment, a **Docker** container has been set and it's available at _Docker Hub_ repository under _marcescandellmari/or-tools_. There's also a _Dockerfile_ for model deployment.
